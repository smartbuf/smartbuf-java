package com.github.sisyphsu.nakedata.proto;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.std.ArrayNode;
import com.github.sisyphsu.nakedata.node.std.ObjectNode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static com.github.sisyphsu.nakedata.proto.Const.*;

/**
 * Output wraps the logic that scans the source data and serializes the output message into highly compressed data
 *
 * @author sulin
 * @since 2019-05-01 14:50:15
 */
public final class Output {

    private final boolean      stream;
    private final Schema       schema;
    private final OutputWriter writer;

    private final OutputDataPool   dataPool;
    private final OutputNamePool   namePool;
    private final OutputStructPool structPool;

    /**
     * Initialize Output, it is reusable
     *
     * @param stream    Underlying output stream
     * @param enableCxt Enable context-model or not
     */
    public Output(OutputStream stream, boolean enableCxt) {
        this.stream = enableCxt;
        this.schema = new Schema(enableCxt);
        this.writer = new OutputWriter(stream);
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
        if (node == null) {
            throw new NullPointerException("node can't be null");
        }
        this.schema.reset();
        this.namePool.reset();
        this.dataPool.reset();
        this.structPool.reset();

        this.scan(node);

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
        this.writeNode(node, writer);
    }

    /**
     * Ouput the specified Node into writer, all nodes need prefix an varuint as head-id.
     */
    private void writeNode(Node node, OutputWriter writer) throws IOException {
        if (node.isNull()) {
            writer.writeVarUint((ID_NULL << 2) | FLAG_DATA);
            return;
        }
        switch (node.dataType()) {
            case BOOL:
                if (node.booleanValue()) {
                    writer.writeVarUint((ID_TRUE << 2) | FLAG_DATA);
                } else {
                    writer.writeVarUint((ID_FALSE << 2) | FLAG_DATA);
                }
                break;
            case FLOAT:
                writer.writeVarUint((dataPool.findFloatID(node.floatValue()) << 2) | FLAG_DATA);
                break;
            case DOUBLE:
                writer.writeVarUint((dataPool.findDoubleID(node.doubleValue()) << 2) | FLAG_DATA);
                break;
            case VARINT:
                writer.writeVarUint((dataPool.findVarintID(node.longValue()) << 2) | FLAG_DATA);
                break;
            case STRING:
                writer.writeVarUint((dataPool.findStringID(node.stringValue()) << 2) | FLAG_DATA);
                break;
            case SYMBOL:
                String symbol = node.stringValue();
                int dataId = stream ? dataPool.findSymbolID(symbol) : dataPool.findStringID(symbol);
                writer.writeVarUint((dataId << 2) | FLAG_DATA);
                break;
            case ARRAY:
                this.writeArrayNode((ArrayNode) node, writer, true);
                break;
            case OBJECT:
                ObjectNode objectNode = (ObjectNode) node;
                String[] fields = objectNode.getFields();
                writer.writeVarUint((structPool.findStructID(fields) << 2) | FLAG_STRUCT);
                for (String field : fields) {
                    this.writeNode(objectNode.getField(field), writer);
                }
                break;
        }
    }

    /**
     * Output the specified ArrayNode into writer, different callers may need different form of head
     *
     * @param suffixFlag Need suffix FLAG_ARRAY at head or not
     */
    private void writeArrayNode(ArrayNode node, OutputWriter writer, boolean suffixFlag) throws IOException {
        List<ArrayNode.Slice> arrayNodes = node.getSlices();
        for (int i = 0, len = arrayNodes.size(); i < len; i++) {
            ArrayNode.Slice slice = arrayNodes.get(i);
            // output array|slice header
            long sliceHead = (slice.size() << 5) | (Const.toSliceType(slice.elementType()) << 1) | ((i == len - 1) ? 0 : 1);
            if (suffixFlag && i == 0) {
                sliceHead = (sliceHead << 2) | FLAG_ARRAY; // only the first slice need bring body-flag
            }
            writer.writeVarUint(sliceHead);
            // output slice body
            switch (slice.elementType()) {
                case NULL:
                    break;
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
                case DOUBLE_NATIVE:
                    writer.writeDoubleArray(slice.asDoubleArray());
                    break;
                case FLOAT:
                    writer.writeFloatSlice(slice.asFloatSlice());
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
                        writer.writeVarUint(stream ? dataPool.findSymbolID(item) : dataPool.findStringID(item));
                    }
                    break;
                case ARRAY:
                    for (ArrayNode item : slice.asArraySlice()) {
                        this.writeArrayNode(item, writer, false);
                    }
                    break;
                case OBJECT:
                    List<ObjectNode> nodes = slice.asObjectSlice();
                    String[] fields = nodes.get(0).getFields();
                    writer.writeVarUint(structPool.findStructID(fields)); // structId
                    for (ObjectNode item : nodes) {
                        for (String field : fields) {
                            this.writeNode(item.getField(field), writer);
                        }
                    }
                    break;
            }
        }
    }

    /**
     * Scan the specified Node's metadata
     */
    private void scan(Node node) {
        if (node.isNull()) {
            return;
        }
        switch (node.dataType()) {
            case FLOAT:
                dataPool.registerFloat(node.floatValue());
                break;
            case DOUBLE:
                dataPool.registerDouble(node.doubleValue());
                break;
            case VARINT:
                dataPool.registerVarint(node.longValue());
                break;
            case STRING:
                dataPool.registerString(node.stringValue());
                break;
            case SYMBOL:
                if (stream) {
                    dataPool.registerSymbol(node.stringValue());
                } else {
                    dataPool.registerString(node.stringValue());
                }
                break;
            case ARRAY:
                this.scanArrayNode((ArrayNode) node);
                break;
            case OBJECT:
                this.scanObjectNode((ObjectNode) node);
                break;
        }
    }

    /**
     * Scan the specified ArrayNode, support all kinds array exclude native array.
     */
    private void scanArrayNode(ArrayNode array) {
        for (ArrayNode.Slice slice : array.getSlices()) {
            switch (slice.elementType()) {
                case STRING:
                    for (String str : slice.asStringSlice()) {
                        dataPool.registerString(str);
                    }
                    break;
                case SYMBOL:
                    for (Object item : slice.asSymbolSlice()) {
                        if (stream) {
                            dataPool.registerSymbol((String) item);
                        } else {
                            dataPool.registerString((String) item);
                        }
                    }
                    break;
                case OBJECT:
                    for (ObjectNode item : slice.asObjectSlice()) {
                        this.scanObjectNode(item);
                    }
                    break;
                case ARRAY:
                    for (ArrayNode item : slice.asArraySlice()) {
                        this.scanArrayNode(item);
                    }
                    break;
            }
        }
    }

    /**
     * Scan the specified ObjectNode, collect it's relevant metadata and children's
     */
    private void scanObjectNode(ObjectNode node) {
        String[] fieldNames = node.getFields();
        boolean stable = node.isStable();
        // reigster object's metadata
        namePool.register(!stream || !stable, fieldNames);
        structPool.register(!stream || !stable, fieldNames);
        // scan children nodes
        for (Node subNode : node.getData().values()) {
            this.scan(subNode);
        }
    }

}
