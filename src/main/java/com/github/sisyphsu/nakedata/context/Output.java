package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.ArrayType;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.node.array.MixArrayNode;
import com.github.sisyphsu.nakedata.node.std.ObjectNode;

import java.io.IOException;
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

    private long version;

    private final boolean          stream;
    private final OutputNamePool   namePool   = new OutputNamePool();
    private final OutputStructPool structPool = new OutputStructPool(1 << 12);
    private final OutputDataPool   dataPool   = new OutputDataPool(1 << 16);

    public Output(boolean enableCxt) {
        this.version = 0L;
        this.stream = enableCxt;
    }

    public void write(Node node, OutputWriter writer) throws IOException {
        if (node == null) {
            throw new NullPointerException("node can't be null");
        }
        namePool.reset();
        dataPool.reset();
        structPool.reset();

        this.scan(node);

        this.writeMeta(writer);
        this.writeNode(node, writer);
    }

    /**
     * 将当前FrameMeta通过writer序列化输出
     */
    private void writeMeta(OutputWriter writer) throws IOException {
        // prepare the first byte to identify metadata
        byte head = VERSION;
        int cxtCount = 0, tmpCount = 0;
        if (stream) {
            head |= FLAG_STREAM;
            if (namePool.cxtNameAdded.size() > 0) cxtCount++;
            if (structPool.cxtStructAdded.size() > 0) cxtCount++;
            if (structPool.cxtStructExpired.size() > 0) cxtCount++;
            if (dataPool.cxtSymbolAdded.size() > 0) cxtCount++;
            if (dataPool.cxtSymbolExpired.size() > 0) cxtCount++;
            if (cxtCount > 0) head |= FLAG_CXT_META;
        }
        if (dataPool.tmpFloats.size() > 0) tmpCount++;
        if (dataPool.tmpDoubles.size() > 0) tmpCount++;
        if (dataPool.tmpVarints.size() > 0) tmpCount++;
        if (dataPool.tmpStrings.size() > 0) tmpCount++;
        if (namePool.tmpNames.size() > 0) tmpCount++;
        if (structPool.tmpStructs.size() > 0) tmpCount++;
        if (tmpCount > 0) head |= FLAG_TMP_META;

        // 1-byte for summary
        writer.writeByte(head);
        // 1-byte for context sequence, optional
        if (stream) {
            writer.writeByte((byte) version);
        }
        int len;
        // output temporary metadata
        if (tmpCount > 0 && (len = dataPool.tmpFloats.size()) > 0) {
            writer.writeMetaHead(len, TMP_FLOAT, --tmpCount == 0);
            for (int i = 0; i < len; i++) {
                writer.writeFloat(dataPool.tmpFloats.get(i));
            }
        }
        if (tmpCount > 0 && (len = dataPool.tmpDoubles.size()) > 0) {
            writer.writeMetaHead(len, TMP_DOUBLE, --tmpCount == 0);
            for (int i = 0; i < len; i++) {
                writer.writeDouble(dataPool.tmpDoubles.get(i));
            }
        }
        if (tmpCount > 0 && (len = dataPool.tmpVarints.size()) > 0) {
            writer.writeMetaHead(len, TMP_VARINT, --tmpCount == 0);
            for (int i = 0; i < len; i++) {
                writer.writeVarInt(dataPool.tmpVarints.get(i));
            }
        }
        if (tmpCount > 0 && (len = dataPool.tmpStrings.size()) > 0) {
            writer.writeMetaHead(len, TMP_STRING, --tmpCount == 0);
            for (int i = 0; i < len; i++) {
                writer.writeString(dataPool.tmpStrings.get(i));
            }
        }
        if (tmpCount > 0 && (len = namePool.tmpNames.size()) > 0) {
            writer.writeMetaHead(len, TMP_NAMES, --tmpCount == 0);
            for (int i = 0; i < len; i++) {
                writer.writeString(namePool.tmpNames.get(i).name);
            }
        }
        if (tmpCount > 0 && (len = structPool.tmpStructs.size()) > 0) {
            writer.writeMetaHead(len, TMP_STRUCTS, --tmpCount == 0);
            for (int i = 0; i < len; i++) {
                String[] fieldNames = structPool.tmpStructs.get(i).names;
                writer.writeVarUint(fieldNames.length);
                for (String fieldName : fieldNames) {
                    writer.writeVarUint(namePool.findNameID(fieldName));
                }
            }
        }
        // output context metadata
        if (cxtCount > 0 && (len = namePool.cxtNameAdded.size()) > 0) {
            writer.writeMetaHead(len, CXT_NAME_ADDED, --cxtCount == 0);
            for (int i = 0; i < len; i++) {
                writer.writeString(namePool.cxtNameAdded.get(i).name);
            }
        }
        if (cxtCount > 0 && (len = structPool.cxtStructAdded.size()) > 0) {
            writer.writeMetaHead(len, CXT_STRUCT_ADDED, --cxtCount == 0);
            for (int i = 0; i < len; i++) {
                String[] fieldNames = structPool.cxtStructAdded.get(i).names;
                writer.writeVarUint(fieldNames.length);
                for (String fieldName : fieldNames) {
                    writer.writeVarUint(namePool.findNameID(fieldName));
                }
            }
        }
        if (cxtCount > 0 && (len = structPool.cxtStructExpired.size()) > 0) {
            writer.writeMetaHead(len, CXT_STRUCT_EXPIRED, --cxtCount == 0);
            for (int i = 0; i < len; i++) {
                writer.writeVarUint(structPool.cxtStructExpired.get(i).offset);
            }
        }
        if (cxtCount > 0 && (len = dataPool.cxtSymbolAdded.size()) > 0) {
            writer.writeMetaHead(len, CXT_SYMBOL_ADDED, --cxtCount == 0);
            for (int i = 0; i < len; i++) {
                writer.writeString(dataPool.cxtSymbolAdded.get(i));
            }
        }
        if (cxtCount > 0 && (len = dataPool.cxtSymbolExpired.size()) > 0) {
            writer.writeMetaHead(len, CXT_SYMBOL_EXPIRED, --cxtCount == 0);
            for (int i = 0; i < len; i++) {
                writer.writeVarUint(dataPool.cxtSymbolExpired.get(i));
            }
        }
    }

    /**
     * 输出报文的body区
     * 节点前缀一个varuint标明：数据类型（普通数据、array、object）
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
                writer.writeBooleanArray(node.booleansValue());
                break;
            case N_BYTE_ARRAY:
                writer.writeByteArray(node.bytesValue());
                break;
            case N_SHORT_ARRAY:
                writer.writeShortArray(node.shortsValue());
                break;
            case N_INT_ARRAY:
                writer.writeIntArray(node.intsValue());
                break;
            case N_LONG_ARRAY:
                writer.writeLongArray(node.longsValue());
                break;
            case N_FLOAT_ARRAY:
                writer.writeFloatArray(node.floatsValue());
                break;
            case N_DOUBLE_ARRAY:
                writer.writeDoubleArray(node.doublesValue());
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
            final boolean hasMore = i == len - 1;
            ArrayNode slice = arrayNodes.get(i);
            List data = slice.getItems();
            switch (slice.elementType()) {
                case BOOL:
                    writer.writeBooleanSlice(data, hasMore);
                    break;
                case BYTE:
                    writer.writeByteSlice(data, hasMore);
                    break;
                case SHORT:
                    writer.writeShortSlice(data, hasMore);
                    break;
                case INT:
                    writer.writeIntSlice(data, hasMore);
                    break;
                case LONG:
                    writer.writeLongSlice(data, hasMore);
                    break;
                case FLOAT:
                    writer.writeFloatSlice(data, hasMore);
                    break;
                case DOUBLE:
                    writer.writeDoubleSlice(data, hasMore);
                    break;
                case NULL:
                    writer.writeSliceHead(data.size(), ArrayType.NULL, hasMore);
                    break;
                case STRING:
                    writer.writeSliceHead(data.size(), ArrayType.STRING, hasMore);
                    for (Object datum : data) {
                        writer.writeVarInt(dataPool.findStringID((String) datum));
                    }
                    break;
                case SYMBOL:
                    if (stream) {
                        writer.writeSliceHead(data.size(), ArrayType.SYMBOL, hasMore);
                        for (Object item : data) {
                            writer.writeVarInt(dataPool.findSymbolID((String) item));
                        }
                    } else {
                        writer.writeSliceHead(data.size(), ArrayType.STRING, hasMore);
                        for (Object item : data) {
                            writer.writeVarInt(dataPool.findStringID((String) item));
                        }
                    }
                    break;
                case ARRAY:
                    writer.writeSliceHead(data.size(), ArrayType.ARRAY, hasMore);
                    for (Object item : data) {
                        this.writeArrayNode((ArrayNode) item, writer);
                    }
                    break;
                case OBJECT:
                    String[] fields = ((ObjectNode) data.get(0)).getFields();
                    writer.writeSliceHead(data.size(), ArrayType.OBJECT, hasMore);
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
