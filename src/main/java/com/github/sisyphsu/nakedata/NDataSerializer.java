package com.github.sisyphsu.nakedata;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.sisyphsu.nakedata.context.ContextVersion;
import com.github.sisyphsu.nakedata.context.output.OutputContext;
import com.github.sisyphsu.nakedata.io.OutputWriter;
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
public class NDataSerializer {

    private static final byte VERSION = 1 << 5;

    private OutputWriter writer;

    private OutputContext context;

    public void serialize(Object obj) {
        JsonNode node = JSONUtils.toJsonNode(obj);
        // step1. 预扫描上下文元数据变化
        ContextVersion version = context.scan(node);
        byte dataType = this.parseType(node);
        // step2. 输出头信息, 包括head、version
        if (version == null) {
            writer.writeByte(dataType);
        } else {
            writer.writeByte((byte) (dataType | VERSION));
            version.doWrite(writer);
        }
        // step3. 输出数据体
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
            case NULL:
                // NULL不需要输出任何数据
                break;
            case BOOLEAN:
                writer.writeBoolean(node.booleanValue());
                break;
            case NUMBER:
                if (node.isFloat()) {
                    writer.writeFloat(node.floatValue());
                } else if (node.isDouble()) {
                    writer.writeDouble(node.doubleValue());
                } else {
                    writer.writeInt(node.asLong());
                }
                break;
            case BINARY:
                writer.writeBinary(node.binaryValue());
                break;
            case STRING:
                writer.writeString(node.textValue());
                break;
            case OBJECT:
                // 对象需要前缀类型
                int id = context.getTypeId(node);
                // 按照封装好的ContextType执行输出
                // step1. 输出type-id
                // step2. 输出null-table —— 不需要输出null-table, 元数据需要改造, 变量表与类型表兼容的模式。
                // step3. 输出fields的值
                break;
            case ARRAY:
                // 数组需要特殊处理
                // 需要考虑数组内成员类型的不兼容，输出成员之前，需要先输出[type-id + count], 再输出子类型
                // step1. 输出[type + count + isEnd] = (3+x)bit
                // step2. 如果type为object, 则输出其type-id
                // step3. 循环输出items
                break;
            default:
                throw new IllegalArgumentException("invalid JsonNode: " + node.getNodeType());
        }
    }

    private byte parseType(JsonNode node) {
        return DataType.OBJECT;
    }

}
