package com.github.sisyphsu.nakedata;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.sisyphsu.nakedata.context.ContextUtils;
import com.github.sisyphsu.nakedata.context.model.ContextVersion;
import com.github.sisyphsu.nakedata.context.output.OutputContext;
import com.github.sisyphsu.nakedata.io.OutputWriter;
import com.github.sisyphsu.nakedata.jackson.node.NArrayNode;
import com.github.sisyphsu.nakedata.jackson.node.NObjectNode;
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
                this.writeObject((NObjectNode) node);
                break;
            case ARRAY:
                this.writeArray((NArrayNode) node);
                break;
            default:
                // 其他类型不需要输出
        }
    }

    // 序列化对象节点
    private void writeObject(NObjectNode node) throws IOException {
        // step1. 输出类型ID
        writer.writeVarInt(node.getType().getId());
        // step2. 输出fields的值
        for (JsonNode field : node.getFields().values()) {
            this.writeNode(field);
        }
    }

    // 序列化数组节点
    private void writeArray(NArrayNode arr) throws IOException {
        // 需要考虑数组内成员类型的不兼容，输出成员之前，需要先输出[type-id + count], 再输出子类型
        int offset = 0;
        for (NArrayNode.Group group : arr.getGroups()) {
            // step1. 输出[count | typecode | isEnd]
            writer.writeVarUint((group.getCount() << 5) | (group.getTypeCode() << 1) | (group.isEnd() ? 0 : 1));
            // step2. 输出type-id
            // issue: null的特殊处理, 可能会导致数组内相同对象的分组不同
            if (group.getType() != null) {
                writer.writeVarInt(group.getType().getId());
            }
            // step3. 循环输出items
            for (int i = 0; i < group.getCount(); i++) {
                this.writeNode(arr.get(offset + i));
            }
            offset += group.getCount();
        }
    }

}
