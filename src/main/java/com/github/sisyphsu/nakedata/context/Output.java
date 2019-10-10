package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.node.array.MixArrayNode;
import com.github.sisyphsu.nakedata.node.std.ObjectNode;

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

    public void write(Node node, OutputWriter writer) {
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
    private void writeMeta(OutputWriter writer) {
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
     */
    private void writeNode(Node node, OutputWriter writer) {
        if (node.isNull()) {
            writer.writeVarInt(ID_NULL);
            return;
        }
        switch (node.dataType()) {
            case BOOL:
                writer.writeVarInt(node.booleanValue() ? ID_TRUE : ID_FALSE);
                break;
            case FLOAT:
                writer.writeVarInt(dataPool.findFloatID(node.floatValue()));
                break;
            case DOUBLE:
                writer.writeVarInt(dataPool.findDoubleID(node.doubleValue()));
                break;
            case VARINT:
                writer.writeVarInt(dataPool.findVarintID(node.longValue()));
                break;
            case STRING:
                writer.writeVarInt(dataPool.findStringID(node.stringValue()));
                break;
            case SYMBOL:
                if (stream) {
                    writer.writeVarInt(dataPool.findSymbolID(node.stringValue()));
                } else {
                    writer.writeVarInt(dataPool.findStringID(node.stringValue()));
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
                this.writeObjectNode((ObjectNode) node, writer);
                break;
        }
    }

    /**
     * 输出ArrayNode
     */
    @SuppressWarnings("unchecked")
    private void writeArrayNode(ArrayNode node, OutputWriter writer) {
        List<ArrayNode> arrayNodes;
        if (node instanceof MixArrayNode) {
            arrayNodes = node.getItems();
        } else {
            arrayNodes = Collections.singletonList(node);
        }
        for (int i = 0, len = arrayNodes.size(); i < len; i++) {
            ArrayNode slice = arrayNodes.get(i);
            switch (slice.elementType()) {
                case NULL:
                    writer.writeNullSlice(slice.getItems(), i == len - 1);
                    break;
                case BOOL:
                    writer.writeBooleanSlice(slice.getItems(), i == len - 1);
                    break;
                case BYTE:
                    writer.writeByteSlice(slice.getItems(), i == len - 1);
                    break;
                case SHORT:
                    writer.writeShortSlice(slice.getItems(), i == len - 1);
                    break;
                case INT:
                    writer.writeIntSlice(slice.getItems(), i == len - 1);
                    break;
                case LONG:
                    writer.writeLongArray(slice.getItems(), i == len - 1);
                    break;
                case FLOAT:
                    writer.writeFloatArray(slice.getItems(), i == len - 1);
                    break;
                case DOUBLE:
                    writer.writeDoubleArray(slice.getItems(), i == len - 1);
                    break;
                case STRING:
                    for (Object item : slice.getItems()) {
                        writer.writeVarInt(dataPool.findStringID((String) item));
                    }
                    break;
                case SYMBOL:
                    if (stream) {
                        slice.getItems().forEach(item -> writer.writeVarInt(dataPool.findSymbolID((String) item)));
                    } else {
                        slice.getItems().forEach(item -> writer.writeVarInt(dataPool.findStringID((String) item)));
                    }
                    break;
                case ARRAY:
                    for (Object item : slice.getItems()) {
                        this.writeArrayNode((ArrayNode) item, writer);
                    }
                    break;
                case OBJECT:
                    writer.writeVarInt(0); // structId
                    for (Object item : slice.getItems()) {
                        this.writeObjectNode((ObjectNode) item, writer);
                    }
                    break;
            }
        }
    }

    /**
     * 输出ObjectNode，按照fields固定顺序，依次输出。
     */
    private void writeObjectNode(ObjectNode node, OutputWriter writer) {
        ObjectNode.Key key = node.getKey();
        // 输出structId
        writer.writeVarUint(structPool.findStructID(key.getFields()));
        // 输出fields，此处不care各个字段的数据类型
        for (String field : key.getFields()) {
            Node subNode = node.getData().get(field);
            this.writeNode(subNode, writer);
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
