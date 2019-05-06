package com.github.sisyphsu.nakedata;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.sisyphsu.nakedata.context.ContextUtils;
import com.github.sisyphsu.nakedata.context.model.ContextVersion;
import com.github.sisyphsu.nakedata.context.output.OutputContext;
import com.github.sisyphsu.nakedata.io.OutputWriter;
import com.github.sisyphsu.nakedata.jackson.ObjectNode;
import com.github.sisyphsu.nakedata.utils.JSONUtils;

import java.io.IOException;

/**
 * 1. scan object's type
 * 2. adjust namePool
 * 3. adjust typePool
 * 4. serialize data: name, type, content
 *
 * @author sulin
 * @since 2019-04-24 21:43:29
 */
public class DataSerializer {

    private static final byte CONTEXT_VERSION = 1 << 5;

    private OutputWriter writer;

    private OutputContext context;

    /**
     * 执行序列化
     *
     * @param obj 待序列化的对象
     * @throws IOException IO异常
     */
    public void serialize(Object obj) throws IOException {
        JsonNode node = JSONUtils.toJsonNode(obj);
        // step1. 扫描元数据变化
        ContextVersion version = context.scan(node);
        byte dataType = DataType.parseType(node);
        // step2. 输出头信息, 包括head、version
        if (version == null) {
            writer.writeByte(dataType);
        } else {
            writer.writeByte((byte) (dataType | CONTEXT_VERSION));
            ContextUtils.doWrite(writer, version);
        }
        // step3. 输出数据体
        this.writeNode(node);
    }

    /**
     * 输出Json节点, 针对树形多层级通过递归输出.
     * <p>
     * 仅输出数据体，通过typeId引用Context中已同步的类型元数据.
     *
     * @param node 待输出的节点
     */
    private void writeNode(JsonNode node) throws IOException {
        switch (node.getNodeType()) {
            case NUMBER:
                if (node.isFloat()) {
                    writer.writeFloat(node.floatValue());
                } else if (node.isDouble()) {
                    writer.writeDouble(node.doubleValue());
                } else {
                    writer.writeVarInt(node.asLong());
                }
                break;
            case BINARY:
                writer.writeBinary(node.binaryValue());
                break;
            case STRING:
                writer.writeString(node.textValue());
                break;
            case OBJECT:
                this.writeObject((ObjectNode) node);
                break;
            case ARRAY:
                this.writeArray((ArrayNode) node);
                break;
            default:
                throw new IllegalArgumentException("invalid JsonNode: " + node.getNodeType());
        }
    }

    // TODO
    private void writeObject(ObjectNode node) {
        // step1. 输出类型ID
        writer.writeVarInt(node.getType().getId());
        // step2. 输出fields的值, 跳过null、true、false
    }

    // TODO
    private void writeArray(ArrayNode node) {
        // 数组需要特殊处理
        // 需要考虑数组内成员类型的不兼容，输出成员之前，需要先输出[type-id + count], 再输出子类型
        // step1. 输出[type + count + isEnd] = (3+x)bit
        // step2. 如果type为object, 则输出其type-id
        // step3. 循环输出items
        // 循环处理, 先扫描类型共享等。
    }

}
