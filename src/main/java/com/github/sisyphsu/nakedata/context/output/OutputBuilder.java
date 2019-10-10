package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.ArrayType;
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
public final class OutputBuilder {

    private long version;

    private final boolean          enableCxt;
    private final OutputNamePool   namePool   = new OutputNamePool();
    private final OutputStructPool structPool = new OutputStructPool(1 << 12);
    private final OutputDataPool   dataPool   = new OutputDataPool(1 << 16);

    public OutputBuilder(boolean enableCxt) {
        this.version = 0L;
        this.enableCxt = enableCxt;
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
        int cxtCount = 0;
        if (enableCxt) {
            if (namePool.cxtNameAdded.size() > 0) cxtCount++;
            if (structPool.cxtStructAdded.size() > 0) cxtCount++;
            if (structPool.cxtStructExpired.size() > 0) cxtCount++;
            if (dataPool.symbolAdded.size() > 0) cxtCount++;
            if (dataPool.symbolExpired.size() > 0) cxtCount++;
        }
        int count = cxtCount;
        if (dataPool.floatArea.size() > 0) count++;
        if (dataPool.doubleArea.size() > 0) count++;
        if (dataPool.varintArea.size() > 0) count++;
        if (dataPool.stringArea.size() > 0) count++;
        if (namePool.tmpNames.size() > 0) count++;
        if (structPool.tmpStructs.size() > 0) count++;

        byte head = VERSION;
        if (enableCxt) {
            head |= FLAG_STREAM;
        }
        if (count == 0) {
            head |= FLAG_HEAD;
        }
        writer.writeByte(head);
        writer.writeByte((byte) version);
        if (count > 0) {
            this.writeTmpMeta(writer, count);
        }
        if (enableCxt && cxtCount > 0) {
            this.writeCxtMeta(writer, cxtCount);
        }
    }

    /**
     * Output body of metadata.
     */
    private void writeTmpMeta(OutputWriter writer, int count) {
        int len = dataPool.floatArea.size();
        if (len > 0) {
            this.writeMetaHead(writer, len, CODE_FLOAT, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeFloat(dataPool.floatArea.get(i));
            }
        }
        len = dataPool.doubleArea.size();
        if (len > 0) {
            this.writeMetaHead(writer, len, CODE_DOUBLE, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeDouble(dataPool.doubleArea.get(i));
            }
        }
        len = dataPool.varintArea.size();
        if (len > 0) {
            this.writeMetaHead(writer, len, CODE_VARINT, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeVarInt(dataPool.varintArea.get(i));
            }
        }
        len = dataPool.stringArea.size();
        if (len > 0) {
            this.writeMetaHead(writer, len, CODE_STRING, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeString(dataPool.stringArea.get(i));
            }
        }
        len = namePool.tmpNames.size();
        if (len > 0) {
            this.writeMetaHead(writer, len, CODE_NAMES, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeString(namePool.tmpNames.get(i).name);
            }
        }
        len = structPool.tmpStructs.size();
        if (len > 0) {
            this.writeMetaHead(writer, len, CODE_STRUCTS, --count == 0);
            for (int i = 0; i < len; i++) {
                String[] fieldNames = structPool.tmpStructs.get(i).names;
                writer.writeVarUint(fieldNames.length);
                for (String fieldName : fieldNames) {
                    writer.writeVarUint(namePool.findNameID(fieldName));
                }
            }
        }
    }

    /**
     * Output context metadata
     */
    private void writeCxtMeta(OutputWriter writer, int count) {
        int len = namePool.cxtNameAdded.size();
        if (len > 0) {
            writeMetaHead(writer, len, CODE_NAME_ADDED, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeString(namePool.cxtNameAdded.get(i).name);
            }
        }
        len = structPool.cxtStructAdded.size();
        if (len > 0) {
            this.writeMetaHead(writer, len, CODE_STRUCT_ADDED, --count == 0);
            for (int i = 0; i < len; i++) {
                String[] fieldNames = structPool.cxtStructAdded.get(i).names;
                writer.writeVarUint(fieldNames.length);
                for (String fieldName : fieldNames) {
                    writer.writeVarUint(namePool.findNameID(fieldName));
                }
            }
        }
        len = structPool.cxtStructExpired.size();
        if (len > 0) {
            this.writeMetaHead(writer, len, CODE_STRUCT_EXPIRED, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeVarUint(structPool.cxtStructExpired.get(i).offset);
            }
        }
        len = dataPool.symbolAdded.size();
        if (len > 0) {
            this.writeMetaHead(writer, len, CODE_SYMBOL_ADDED, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeString(dataPool.symbolAdded.get(i));
            }
        }
        len = dataPool.symbolExpired.size();
        if (len > 0) {
            this.writeMetaHead(writer, len, CODE_SYMBOL_EXPIRED, --count == 0);
            for (int i = 0; i < len; i++) {
                writer.writeVarUint(dataPool.symbolExpired.get(i));
            }
        }
    }

    /**
     * Output the head of one schema area.
     */
    private void writeMetaHead(OutputWriter writer, long size, int code, boolean hasMore) {
        writer.writeVarUint((size << 5) | (code << 1) | (hasMore ? 1 : 0));
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
                if (enableCxt) {
                    writer.writeVarInt(dataPool.findSymbolID(node.stringValue()));
                } else {
                    writer.writeVarInt(dataPool.findStringID(node.stringValue()));
                }
                break;
            case N_BOOL_ARRAY:
                writer.writeArrayMeta(true, ArrayType.BOOL, node.booleansValue().length);
                writer.writeBooleanArray(node.booleansValue());
                break;
            case N_BYTE_ARRAY:
                writer.writeArrayMeta(true, ArrayType.BYTE, node.bytesValue().length);
                writer.writeByteArray(node.bytesValue());
                break;
            case N_SHORT_ARRAY:
                writer.writeArrayMeta(true, ArrayType.SHORT, node.shortsValue().length);
                writer.writeShortArray(node.shortsValue());
                break;
            case N_INT_ARRAY:
                writer.writeArrayMeta(true, ArrayType.INT, node.intsValue().length);
                writer.writeIntArray(node.intsValue());
                break;
            case N_LONG_ARRAY:
                writer.writeArrayMeta(true, ArrayType.LONG, node.longsValue().length);
                writer.writeLongArray(node.longsValue());
                break;
            case N_FLOAT_ARRAY:
                writer.writeArrayMeta(true, ArrayType.FLOAT, node.floatsValue().length);
                writer.writeFloatArray(node.floatsValue());
                break;
            case N_DOUBLE_ARRAY:
                writer.writeArrayMeta(true, ArrayType.DOUBLE, node.doublesValue().length);
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
            writer.writeArrayMeta(i == len - 1, slice.elementType(), slice.size());
            switch (slice.elementType()) {
                case BOOL:
                    writer.writeBooleanArray(slice.getItems());
                    break;
                case BYTE:
                    writer.writeByteArray(slice.getItems());
                    break;
                case SHORT:
                    writer.writeShortArray(slice.getItems());
                    break;
                case INT:
                    writer.writeIntArray(slice.getItems());
                    break;
                case LONG:
                    writer.writeLongArray(slice.getItems());
                    break;
                case FLOAT:
                    writer.writeFloatArray(slice.getItems());
                    break;
                case DOUBLE:
                    writer.writeDoubleArray(slice.getItems());
                    break;
                case STRING:
                    for (Object item : slice.getItems()) {
                        writer.writeVarInt(dataPool.findStringID((String) item));
                    }
                    break;
                case SYMBOL:
                    if (enableCxt) {
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
                if (enableCxt) {
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
                if (enableCxt) {
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
        namePool.register(!enableCxt || !stable, fieldNames);
        structPool.register(!enableCxt || !stable, fieldNames);
        // 扫描子节点
        for (Node subNode : node.getData().values()) {
            this.scan(subNode);
        }
    }

}
