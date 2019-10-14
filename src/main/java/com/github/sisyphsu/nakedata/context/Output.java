package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.ArrayType;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.node.array.MixArrayNode;
import com.github.sisyphsu.nakedata.node.std.ObjectNode;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;

import static com.github.sisyphsu.nakedata.context.Proto.*;

/**
 * 输出构造器，负责扫描源数据，序列化输出报文
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

    public Output(OutputStream stream, boolean enableCxt) {
        this.stream = enableCxt;
        this.schema = new Schema(enableCxt);
        this.writer = new OutputWriter(stream);
        this.dataPool = new OutputDataPool(schema, 1 << 16);
        this.namePool = new OutputNamePool(schema);
        this.structPool = new OutputStructPool(schema, 1 << 12);
    }

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
     * 输出报文的body区，节点前缀一个varuint标明：数据类型（普通数据、array、object）
     */
    private void writeNode(Node node, OutputWriter writer) throws IOException {
        if (node.isNull()) {
            writer.writeVarUint((ID_NULL << 2) | BODY_FLAG_DATA);
            return;
        }
        switch (node.dataType()) {
            case BOOL:
                if (node.booleanValue()) {
                    writer.writeVarUint((ID_TRUE << 2) | BODY_FLAG_DATA);
                } else {
                    writer.writeVarUint((ID_FALSE << 2) | BODY_FLAG_DATA);
                }
                break;
            case FLOAT:
                writer.writeVarUint((dataPool.findFloatID(node.floatValue()) << 2) | BODY_FLAG_DATA);
                break;
            case DOUBLE:
                writer.writeVarUint((dataPool.findDoubleID(node.doubleValue()) << 2) | BODY_FLAG_DATA);
                break;
            case VARINT:
                writer.writeVarUint((dataPool.findVarintID(node.longValue()) << 2) | BODY_FLAG_DATA);
                break;
            case STRING:
                writer.writeVarUint((dataPool.findStringID(node.stringValue()) << 2) | BODY_FLAG_DATA);
                break;
            case SYMBOL:
                if (stream) {
                    writer.writeVarUint((dataPool.findSymbolID(node.stringValue()) << 2) | BODY_FLAG_DATA);
                } else {
                    writer.writeVarUint((dataPool.findStringID(node.stringValue()) << 2) | BODY_FLAG_DATA);
                }
                break;
            case N_BOOL_ARRAY:
                boolean[] booleans = node.booleansValue();
                writer.writeVarUint((booleans.length << 7) | (ArrayType.BOOL.getCode() << 3) | BODY_FLAG_ARRAY);
                writer.writeBooleanArray(booleans);
                break;
            case N_BYTE_ARRAY:
                byte[] bytes = node.bytesValue();
                writer.writeVarUint((bytes.length << 7) | (ArrayType.BYTE.getCode() << 3) | BODY_FLAG_ARRAY);
                writer.writeByteArray(bytes);
                break;
            case N_SHORT_ARRAY:
                short[] shorts = node.shortsValue();
                writer.writeVarUint((shorts.length << 7) | (ArrayType.SHORT.getCode() << 3) | BODY_FLAG_ARRAY);
                writer.writeShortArray(shorts);
                break;
            case N_INT_ARRAY:
                int[] ints = node.intsValue();
                writer.writeVarUint((ints.length << 7) | (ArrayType.INT.getCode() << 3) | BODY_FLAG_ARRAY);
                writer.writeIntArray(ints);
                break;
            case N_LONG_ARRAY:
                long[] longs = node.longsValue();
                writer.writeVarUint((longs.length << 7) | (ArrayType.LONG.getCode() << 3) | BODY_FLAG_ARRAY);
                writer.writeLongArray(longs);
                break;
            case N_FLOAT_ARRAY:
                float[] floats = node.floatsValue();
                writer.writeVarUint((floats.length << 7) | (ArrayType.FLOAT.getCode() << 3) | BODY_FLAG_ARRAY);
                writer.writeFloatArray(floats);
                break;
            case N_DOUBLE_ARRAY:
                double[] doubles = node.doublesValue();
                writer.writeVarUint((doubles.length << 7) | (ArrayType.DOUBLE.getCode() << 3) | BODY_FLAG_ARRAY);
                writer.writeDoubleArray(doubles);
                break;
            case ARRAY:
                this.writeArrayNode((ArrayNode) node, writer);
                break;
            case OBJECT:
                ObjectNode objectNode = (ObjectNode) node;
                String[] fields = objectNode.getFields();
                writer.writeVarUint((structPool.findStructID(fields) << 2) | BODY_FLAG_STRUCT);
                for (String field : fields) {
                    this.writeNode(objectNode.getField(field), writer);
                }
                break;
        }
    }

    /**
     * 输出ArrayNode
     */
    @SuppressWarnings("unchecked")
    private void writeArrayNode(ArrayNode node, OutputWriter writer) throws IOException {
        List<ArrayNode> arrayNodes;
        if (node instanceof MixArrayNode) {
            arrayNodes = node.getItems();
        } else {
            arrayNodes = Collections.singletonList(node);
        }
        for (int i = 0, len = arrayNodes.size(); i < len; i++) {
            ArrayNode slice = arrayNodes.get(i);
            List data = slice.getItems();
            final boolean hasMore = i == len - 1;
            final int dataLen = data.size();
            final byte moreFlag = (byte) (hasMore ? 0b00000100 : 0);
            switch (slice.elementType()) {
                case BOOL:
                    writer.writeVarUint((dataLen << 7) | (ArrayType.BOOL.getCode() << 3) | moreFlag | BODY_FLAG_ARRAY);
                    writer.writeBooleanSlice(data);
                    break;
                case BYTE:
                    writer.writeVarUint((dataLen << 7) | (ArrayType.BYTE.getCode() << 3) | moreFlag | BODY_FLAG_ARRAY);
                    writer.writeByteSlice(data);
                    break;
                case SHORT:
                    writer.writeVarUint((dataLen << 7) | (ArrayType.SHORT.getCode() << 3) | moreFlag | BODY_FLAG_ARRAY);
                    writer.writeShortSlice(data);
                    break;
                case INT:
                    writer.writeVarUint((dataLen << 7) | (ArrayType.INT.getCode() << 3) | moreFlag | BODY_FLAG_ARRAY);
                    writer.writeIntSlice(data);
                    break;
                case LONG:
                    writer.writeVarUint((dataLen << 7) | (ArrayType.LONG.getCode() << 3) | moreFlag | BODY_FLAG_ARRAY);
                    writer.writeLongSlice(data);
                    break;
                case FLOAT:
                    writer.writeVarUint((dataLen << 7) | (ArrayType.FLOAT.getCode() << 3) | moreFlag | BODY_FLAG_ARRAY);
                    writer.writeFloatSlice(data);
                    break;
                case DOUBLE:
                    writer.writeVarUint((dataLen << 7) | (ArrayType.DOUBLE.getCode() << 3) | moreFlag | BODY_FLAG_ARRAY);
                    writer.writeDoubleSlice(data);
                    break;
                case NULL:
                    writer.writeVarUint((dataLen << 7) | (ArrayType.NULL.getCode() << 3) | moreFlag | BODY_FLAG_ARRAY);
                    break;
                case STRING:
                    writer.writeVarUint((dataLen << 7) | (ArrayType.STRING.getCode() << 3) | moreFlag | BODY_FLAG_ARRAY);
                    for (Object datum : data) {
                        writer.writeVarInt(dataPool.findStringID((String) datum));
                    }
                    break;
                case SYMBOL:
                    if (stream) {
                        writer.writeVarUint((dataLen << 7) | (ArrayType.SYMBOL.getCode() << 3) | moreFlag | BODY_FLAG_ARRAY);
                        for (Object item : data) {
                            writer.writeVarInt(dataPool.findSymbolID((String) item));
                        }
                    } else {
                        writer.writeVarUint((dataLen << 7) | (ArrayType.STRING.getCode() << 3) | moreFlag | BODY_FLAG_ARRAY);
                        for (Object item : data) {
                            writer.writeVarInt(dataPool.findStringID((String) item));
                        }
                    }
                    break;
                case ARRAY:
                    writer.writeVarUint((dataLen << 7) | (ArrayType.ARRAY.getCode() << 3) | moreFlag | BODY_FLAG_ARRAY);
                    for (Object item : data) {
                        this.writeArrayNode((ArrayNode) item, writer);
                    }
                    break;
                case OBJECT:
                    String[] fields = ((ObjectNode) data.get(0)).getFields();
                    writer.writeVarUint((dataLen << 7) | (ArrayType.OBJECT.getCode() << 3) | moreFlag | BODY_FLAG_ARRAY);
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
     * 扫描元数据
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
     * 扫描数组节点
     */
    private void scanArrayNode(ArrayNode array) {
        if (array instanceof MixArrayNode) {
            array.forEach(item -> this.scanArrayNode((ArrayNode) item));
            return;
        }
        // 数组成员是string、symbol、object则需要更新元数据
        switch (array.elementType()) {
            case STRING:
                array.forEach(item -> dataPool.registerString(String.valueOf(item)));
                break;
            case SYMBOL:
                if (stream) {
                    array.forEach(item -> dataPool.registerSymbol(String.valueOf(item)));
                } else {
                    array.forEach(item -> dataPool.registerSymbol(String.valueOf(item)));
                }
                break;
            case OBJECT:
                array.forEach(item -> this.scanObjectNode((ObjectNode) item));
                break;
            case ARRAY:
                array.forEach(item -> this.scanArrayNode((ArrayNode) item));
                break;
        }
    }

    /**
     * 扫描并整理Object节点的元数据
     */
    private void scanObjectNode(ObjectNode node) {
        String[] fieldNames = node.getKey().getFields();
        boolean stable = node.getKey().isStable();
        // 注册struct
        namePool.register(!stream || !stable, fieldNames);
        structPool.register(!stream || !stable, fieldNames);
        // 扫描子节点
        for (Node subNode : node.getData().values()) {
            this.scan(subNode);
        }
    }

}
