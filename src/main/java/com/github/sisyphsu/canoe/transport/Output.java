package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.IOWriter;
import com.github.sisyphsu.canoe.node.Node;
import com.github.sisyphsu.canoe.node.std.*;

import java.io.IOException;
import java.util.List;

import static com.github.sisyphsu.canoe.transport.Const.*;

/**
 * Output wraps the logic that scans the source data and serializes the output message into highly compressed data
 *
 * @author sulin
 * @since 2019-05-01 14:50:15
 */
public final class Output {

    private final boolean      enableStreamMode;
    private final Schema       schema;
    private final OutputWriter writer;

    private final OutputDataPool   dataPool;
    private final OutputNamePool   namePool;
    private final OutputStructPool structPool;

    /**
     * Initialize Output, it is reusable
     *
     * @param writer           Underlying output stream
     * @param enableStreamMode Enable stream-model or not
     */
    public Output(IOWriter writer, boolean enableStreamMode) {
        this.enableStreamMode = enableStreamMode;
        this.schema = new Schema(enableStreamMode);
        this.writer = new OutputWriter(writer);
        this.dataPool = new OutputDataPool(schema, 1 << 16);
        this.namePool = new OutputNamePool(schema);
        this.structPool = new OutputStructPool(schema, 1 << 12);
    }

    /**
     * Write the specified Node into Output.
     * It will scan metadata first, build {@link Schema} and write it at the beginning of data-packet
     * After that, it will write node as body, which is highly compressed based on precollected schema
     *
     * @param node The node need to output
     * @throws IOException Underlying io error
     */
    public void write(Node node) throws IOException {
        this.schema.reset();
        this.namePool.reset();
        this.dataPool.reset();
        this.structPool.reset();

        Object data = node;
        if (data instanceof BooleanNode) {
            data = ((BooleanNode) data).booleanValue();
        } else if (data instanceof FloatNode) {
            data = ((FloatNode) data).floatValue();
        } else if (data instanceof DoubleNode) {
            data = ((DoubleNode) data).doubleValue();
        } else if (data instanceof StringNode) {
            data = ((StringNode) data).stringValue();
        } else if (data instanceof VarintNode) {
            data = ((VarintNode) data).longValue();
        }

        this.doScan(data);

        for (int i = 0, len = namePool.tmpNames.size(); i < len; i++) {
            schema.tmpNames.add(namePool.tmpNames.get(i).name);
        }
        for (int i = 0, len = namePool.cxtNameAdded.size(); i < len; i++) {
            schema.cxtNameAdded.add(namePool.cxtNameAdded.get(i).name);
        }
        for (int i = 0, len = structPool.tmpStructs.size(); i < len; i++) {
            String[] names = structPool.tmpStructs.get(i).names;
            int[] nameIds = new int[names.length];
            for (int j = 0; j < names.length; j++) {
                nameIds[j] = namePool.findNameID(names[j]);
            }
            schema.tmpStructs.add(nameIds);
        }
        for (int i = 0, len = structPool.cxtStructAdded.size(); i < len; i++) {
            String[] names = structPool.cxtStructAdded.get(i).names;
            int[] nameIds = new int[names.length];
            for (int j = 0; j < names.length; j++) {
                nameIds[j] = namePool.findNameID(names[j]);
            }
            schema.cxtStructAdded.add(nameIds);
        }

        schema.output(writer);
        this.doWrite(data, writer);
    }

    /**
     * Ouput the specified Node into writer, all nodes need prefix an varuint as head-id.
     */
    void doWrite(Object data, OutputWriter writer) throws IOException {
        if (data == null) {
            writer.writeVarUint((ID_NULL << 2) | FLAG_DATA);
        } else if (data instanceof Boolean) {
            byte code = ((Boolean) data) ? ID_TRUE : ID_FALSE;
            writer.writeVarUint((code << 2) | FLAG_DATA);
        } else if (data instanceof Float) {
            writer.writeVarUint((dataPool.findFloatID((Float) data) << 2) | FLAG_DATA);
        } else if (data instanceof Double) {
            writer.writeVarUint((dataPool.findDoubleID((Double) data) << 2) | FLAG_DATA);
        } else if (data instanceof Number) {
            long varint = ((Number) data).longValue();
            writer.writeVarUint((dataPool.findVarintID(varint) << 2) | FLAG_DATA);
        } else if (data instanceof String) {
            writer.writeVarUint((dataPool.findStringID((String) data) << 2) | FLAG_DATA);
        } else if (data instanceof SymbolNode) {
            String symbol = ((SymbolNode) data).stringValue();
            int dataId = enableStreamMode ? dataPool.findSymbolID(symbol) : dataPool.findStringID(symbol);
            writer.writeVarUint((dataId << 2) | FLAG_DATA);
        } else if (data instanceof ObjectNode) {
            ObjectNode objectNode = (ObjectNode) data;
            String[] fields = objectNode.keys();
            writer.writeVarUint((structPool.findStructID(fields) << 2) | FLAG_STRUCT);
            for (Object tmp : objectNode.values()) {
                this.doWrite(tmp, writer);
            }
        } else if (data instanceof ArrayNode) {
            ArrayNode node = (ArrayNode) data;
            if (node == ArrayNode.EMPTY) {
                writer.writeVarUint((ID_ZERO_ARRAY << 2) | FLAG_DATA);
            } else {
                this.doWriteArray(node, writer, true);
            }
        } else {
            throw new UnsupportedOperationException("Unsupported data: " + data);
        }
    }

    /**
     * Output the specified ArrayNode into writer, different callers may need different form of head
     *
     * @param suffixFlag Need suffix FLAG_ARRAY at head or not
     */
    void doWriteArray(ArrayNode node, OutputWriter writer, boolean suffixFlag) throws IOException {
        ArrayNode.Slice[] slices = node.slices();
        for (int i = 0, len = node.size(); i < len; i++) {
            ArrayNode.Slice slice = slices[i];
            // output array|slice header
            long sliceHead = (slice.size() << 5) | (Const.toSliceType(slice.type()) << 1) | ((i == len - 1) ? 0 : 1);
            if (suffixFlag && i == 0) {
                sliceHead = (sliceHead << 2) | FLAG_ARRAY; // only the first slice need bring body-flag
            }
            writer.writeVarUint(sliceHead);
            // output slice body
            switch (slice.type()) {
                case BOOL_NATIVE:
                    writer.writeBooleanArray(slice.asBooleanArray());
                    break;
                case BOOL:
                    writer.writeBooleanSlice(slice.asBoolSlice());
                    break;
                case BYTE_NATIVE:
                    writer.writeByteArray(slice.asByteArray());
                    break;
                case BYTE:
                    writer.writeByteSlice(slice.asByteSlice());
                    break;
                case SHORT_NATIVE:
                    writer.writeShortArray(slice.asShortArray());
                    break;
                case SHORT:
                    writer.writeShortSlice(slice.asShortSlice());
                    break;
                case INT_NATIVE:
                    writer.writeIntArray(slice.asIntArray());
                    break;
                case INT:
                    writer.writeIntSlice(slice.asIntSlice());
                    break;
                case LONG_NATIVE:
                    writer.writeLongArray(slice.asLongArray());
                    break;
                case LONG:
                    writer.writeLongSlice(slice.asLongSlice());
                    break;
                case FLOAT_NATIVE:
                    writer.writeFloatArray(slice.asFloatArray());
                    break;
                case FLOAT:
                    writer.writeFloatSlice(slice.asFloatSlice());
                    break;
                case DOUBLE_NATIVE:
                    writer.writeDoubleArray(slice.asDoubleArray());
                    break;
                case DOUBLE:
                    writer.writeDoubleSlice(slice.asDoubleSlice());
                    break;
                case STRING:
                    for (String datum : slice.asStringSlice()) {
                        writer.writeVarUint(dataPool.findStringID(datum));
                    }
                    break;
                case SYMBOL:
                    for (String item : slice.asSymbolSlice()) {
                        writer.writeVarUint(enableStreamMode ? dataPool.findSymbolID(item) : dataPool.findStringID(item));
                    }
                    break;
                case ARRAY:
                    for (ArrayNode item : slice.asArraySlice()) {
                        if (item == ArrayNode.EMPTY) {
                            writer.writeVarUint(ID_ZERO_ARRAY);
                        } else {
                            this.doWriteArray(item, writer, false);
                        }
                    }
                    break;
                case OBJECT:
                    List<ObjectNode> nodes = slice.asObjectSlice();
                    String[] fields = nodes.get(0).keys();
                    writer.writeVarUint(structPool.findStructID(fields)); // structId
                    for (ObjectNode item : nodes) {
                        for (Object tmp : item.values()) {
                            this.doWrite(tmp, writer);
                        }
                    }
                    break;
                default:
                    // NULL, do nothing
            }
        }
    }

    /**
     * Scan the specified Node's metadata
     */
    private void doScan(Object data) {
        if (data instanceof Float) {
            dataPool.registerFloat((Float) data);
        } else if (data instanceof Double) {
            dataPool.registerDouble((Double) data);
        } else if (data instanceof Long) {
            dataPool.registerVarint(((Long) data));
        } else if (data instanceof String) {
            dataPool.registerString((String) data);
        } else if (data instanceof SymbolNode) {
            String symbol = ((SymbolNode) data).stringValue();
            if (enableStreamMode) {
                dataPool.registerSymbol(symbol);
            } else {
                dataPool.registerString(symbol);
            }
        } else if (data instanceof ObjectNode) {
            this.doScanObject((ObjectNode) data);
        } else if (data instanceof ArrayNode) {
            this.doScanArray((ArrayNode) data);
        }
    }

    /**
     * Scan the specified ArrayNode, support all kinds array exclude native array.
     */
    private void doScanArray(ArrayNode array) {
        ArrayNode.Slice[] slices = array.slices();
        for (int i = 0, len = array.size(); i < len; i++) {
            ArrayNode.Slice slice = slices[i];
            switch (slice.type()) {
                case STRING:
                    for (String str : slice.asStringSlice()) {
                        dataPool.registerString(str);
                    }
                    break;
                case SYMBOL:
                    for (Object item : slice.asSymbolSlice()) {
                        if (enableStreamMode) {
                            dataPool.registerSymbol((String) item);
                        } else {
                            dataPool.registerString((String) item);
                        }
                    }
                    break;
                case OBJECT:
                    for (ObjectNode item : slice.asObjectSlice()) {
                        this.doScanObject(item);
                    }
                    break;
                case ARRAY:
                    for (ArrayNode item : slice.asArraySlice()) {
                        this.doScanArray(item);
                    }
                    break;
            }
        }
    }

    /**
     * Scan the specified ObjectNode, collect it's relevant metadata and children's
     */
    private void doScanObject(ObjectNode node) {
        String[] fieldNames = node.keys();
        boolean stable = node.isStable();
        // reigster object's metadata
        namePool.register(!enableStreamMode || !stable, fieldNames);
        structPool.register(!enableStreamMode || !stable, fieldNames);
        // scan children nodes
        for (Object o : node.values()) {
            this.doScan(o);
        }
    }

}
