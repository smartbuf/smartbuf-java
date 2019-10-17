package com.github.sisyphsu.nakedata.proto;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.node.array.SliceNode;
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
                if (stream) {
                    writer.writeVarUint((dataPool.findSymbolID(node.stringValue()) << 2) | FLAG_DATA);
                } else {
                    writer.writeVarUint((dataPool.findStringID(node.stringValue()) << 2) | FLAG_DATA);
                }
                break;
            case N_BOOL_ARRAY:
                boolean[] booleans = node.booleansValue();
                writer.writeVarUint((booleans.length << 7) | (SLICE_BOOL << 3) | FLAG_ARRAY);
                writer.writeBooleanArray(booleans);
                break;
            case N_BYTE_ARRAY:
                byte[] bytes = node.bytesValue();
                writer.writeVarUint((bytes.length << 7) | (SLICE_BYTE << 3) | FLAG_ARRAY);
                writer.writeByteArray(bytes);
                break;
            case N_SHORT_ARRAY:
                short[] shorts = node.shortsValue();
                writer.writeVarUint((shorts.length << 7) | (SLICE_SHORT << 3) | FLAG_ARRAY);
                writer.writeShortArray(shorts);
                break;
            case N_INT_ARRAY:
                int[] ints = node.intsValue();
                writer.writeVarUint((ints.length << 7) | (SLICE_INT << 3) | FLAG_ARRAY);
                writer.writeIntArray(ints);
                break;
            case N_LONG_ARRAY:
                long[] longs = node.longsValue();
                writer.writeVarUint((longs.length << 7) | (SLICE_LONG << 3) | FLAG_ARRAY);
                writer.writeLongArray(longs);
                break;
            case N_FLOAT_ARRAY:
                float[] floats = node.floatsValue();
                writer.writeVarUint((floats.length << 7) | (SLICE_FLOAT << 3) | FLAG_ARRAY);
                writer.writeFloatArray(floats);
                break;
            case N_DOUBLE_ARRAY:
                double[] doubles = node.doublesValue();
                writer.writeVarUint((doubles.length << 7) | (SLICE_DOUBLE << 3) | FLAG_ARRAY);
                writer.writeDoubleArray(doubles);
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
    @SuppressWarnings("unchecked")
    private void writeArrayNode(ArrayNode node, OutputWriter writer, boolean suffixFlag) throws IOException {
        List<SliceNode> arrayNodes = node.getSlices();
        for (int i = 0, len = arrayNodes.size(); i < len; i++) {
            SliceNode slice = arrayNodes.get(i);
            List data = slice.getItems();
            // output array|slice header
            long sliceHead = (data.size() << 5) | (Const.toSliceType(slice.elementType()) << 1) | ((i == len - 1) ? 0 : 1);
            if (suffixFlag && i == 0) {
                sliceHead = (sliceHead << 2) | FLAG_ARRAY; // only the first slice need bring body-flag
            }
            writer.writeVarUint(sliceHead);
            // output slice body
            switch (slice.elementType()) {
                case BOOL:
                    writer.writeBooleanSlice(data);
                    break;
                case BYTE:
                    writer.writeByteSlice(data);
                    break;
                case SHORT:
                    writer.writeShortSlice(data);
                    break;
                case INT:
                    writer.writeIntSlice(data);
                    break;
                case LONG:
                    writer.writeLongSlice(data);
                    break;
                case FLOAT:
                    writer.writeFloatSlice(data);
                    break;
                case DOUBLE:
                    writer.writeDoubleSlice(data);
                    break;
                case STRING:
                    for (Object datum : data) {
                        writer.writeVarUint(dataPool.findStringID((String) datum));
                    }
                    break;
                case SYMBOL:
                    for (Object item : data) {
                        String str = (String) item;
                        writer.writeVarUint(stream ? dataPool.findSymbolID(str) : dataPool.findStringID(str));
                    }
                    break;
                case ARRAY:
                    for (Object item : data) {
                        this.writeArrayNode((ArrayNode) item, writer, false);
                    }
                    break;
                case OBJECT:
                    String[] fields = ((ObjectNode) data.get(0)).getFields();
                    writer.writeVarUint(structPool.findStructID(fields)); // structId
                    for (Object item : data) {
                        ObjectNode objectNode = (ObjectNode) item;
                        for (String field : fields) {
                            this.writeNode(objectNode.getField(field), writer);
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
        for (SliceNode slice : array.getSlices()) {
            switch (slice.elementType()) {
                case STRING:
                    slice.forEach(item -> dataPool.registerString(String.valueOf(item)));
                    break;
                case SYMBOL:
                    for (Object item : slice.getItems()) {
                        if (stream) {
                            dataPool.registerSymbol((String) item);
                        } else {
                            dataPool.registerString((String) item);
                        }
                    }
                    break;
                case OBJECT:
                    slice.forEach(item -> this.scanObjectNode((ObjectNode) item));
                    break;
                case ARRAY:
                    slice.forEach(item -> this.scanArrayNode((ArrayNode) item));
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
