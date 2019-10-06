package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.ArrayType;
import com.github.sisyphsu.nakedata.context.model.FrameMeta;
import com.github.sisyphsu.nakedata.io.OutputWriter;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.node.array.MixArrayNode;
import com.github.sisyphsu.nakedata.node.std.ObjectNode;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static com.github.sisyphsu.nakedata.context.Proto.*;

/**
 * 输出构造器，负责扫描源数据，序列化输出报文
 *
 * @author sulin
 * @since 2019-05-01 14:50:15
 */
public final class OutputBuilder {

    private static final Pattern NAME = Pattern.compile("^[A-Za-z_$][\\w$]{0,63}$");

    private long version;

    private final boolean      enableCxt;
    private final OutputSchema schema;
    private final FrameMeta    frameMeta;

    public OutputBuilder(boolean enableCxt) {
        this.version = 0L;
        this.enableCxt = enableCxt;

        this.frameMeta = new FrameMeta(true, enableCxt);
        this.schema = new OutputSchema(frameMeta);
    }

    public void buildOutput(Node node, OutputWriter writer) {
        if (node == null) {
            throw new NullPointerException("node can't be null");
        }
        // 构建FrameMeta
        FrameMeta meta = this.scan(node);
        meta.write(writer);

        // 输出数据
        this.writeNode(node, writer);
    }

    /**
     * 输出报文的body区
     */
    public void writeNode(Node node, OutputWriter writer) {
        if (node.isNull()) {
            writer.writeVarInt(ID_NULL);
            return;
        }
        switch (node.dataType()) {
            case BOOL:
                writer.writeVarInt(node.booleanValue() ? ID_TRUE : ID_FALSE);
                break;
            case FLOAT:
                writer.writeVarInt(schema.findFloatID(node.floatValue()));
                break;
            case DOUBLE:
                writer.writeVarInt(schema.findDoubleID(node.doubleValue()));
                break;
            case VARINT:
                writer.writeVarInt(schema.findVarintID(node.longValue()));
                break;
            case STRING:
                writer.writeVarInt(schema.findStringID(node.stringValue()));
                break;
            case SYMBOL:
                writer.writeVarInt(schema.findSymbolID(node.stringValue()));
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
    void writeArrayNode(ArrayNode node, OutputWriter writer) {
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
                        writer.writeVarInt(schema.findStringID((String) item));
                    }
                    break;
                case SYMBOL:
                    for (Object item : slice.getItems()) {
                        writer.writeVarInt(schema.findSymbolID((String) item));
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
    void writeObjectNode(ObjectNode node, OutputWriter writer) {
        String[] fields = node.getFields().keySet().toArray(new String[0]);
        // 输出structId
        writer.writeVarUint(schema.findStructID(fields));
        // 输出fields，此处不care各个字段的数据类型
        for (String field : fields) {
            Node subNode = node.getFields().get(field);
            this.writeNode(subNode, writer);
        }
    }

    /**
     * 扫描元数据. 数据序列化之前扫描收集"变量名"的增量变化, 用于预处理NamePool以及甄别map与object。
     */
    FrameMeta scan(Node node) {
        if (enableCxt) {
            schema.preRelease();
        }
        this.frameMeta.reset((int) ((this.version++) % Integer.MAX_VALUE));

        // 执行扫描
        this.doScan(node);

        return frameMeta;
    }

    /**
     * 扫描元数据
     */
    private void doScan(Node node) {
        if (node.isNull()) {
            return;
        }
        switch (node.dataType()) {
            case FLOAT:
                frameMeta.getTmpFloatArea().add(node.floatValue());
                break;
            case DOUBLE:
                frameMeta.getTmpDoubleArea().add(node.doubleValue());
                break;
            case VARINT:
                frameMeta.getTmpVarintArea().add(node.longValue());
                break;
            case STRING:
                frameMeta.getTmpStringArea().add(node.stringValue());
                break;
            case SYMBOL:
                schema.addSymbol(node.stringValue());
                break;
            case ARRAY:
                this.doScanArrayNode((ArrayNode) node);
                break;
            case OBJECT:
                this.doScanObjectNode((ObjectNode) node);
                break;
        }
    }

    /**
     * 扫描数组节点
     */
    private void doScanArrayNode(ArrayNode array) {
        if (array instanceof MixArrayNode) {
            for (Object item : array.getItems()) {
                this.doScanArrayNode((ArrayNode) item);
            }
            return;
        }
        // 数组成员是string、symbol、object则需要更新元数据
        switch (array.elementType()) {
            case STRING:
                for (Object item : array.getItems()) {
                    frameMeta.getTmpStringArea().add((String) item);
                }
                break;
            case SYMBOL:
                for (Object item : array.getItems()) {
                    schema.addSymbol(String.valueOf(item));
                }
                break;
            case OBJECT:
                for (Object item : array.getItems()) {
                    this.doScanObjectNode((ObjectNode) item);
                }
                break;
            case ARRAY:
                for (Object item : array.getItems()) {
                    this.doScanArrayNode((ArrayNode) item);
                }
                break;
        }
    }

    /**
     * 扫描并整理Object节点的元数据
     */
    private void doScanObjectNode(ObjectNode node) {
        String[] fields = node.getFields().keySet().toArray(new String[0]);
        // 注册struct
        boolean isTmp = !enableCxt;
        for (int i = 0; i < fields.length && !isTmp; i++) {
            isTmp = NAME.matcher(fields[i]).matches();
        }
        schema.addStruct(fields);
        // 扫描子节点
        for (Node subNode : node.getFields().values()) {
            this.doScan(subNode);
        }
    }

}
