package com.github.sisyphsu.nakedata.context.output;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 输出上下文
 *
 * @author sulin
 * @since 2019-05-01 14:50:15
 */
public class OutputContext {

    private List<String> names;

    private List<TypeRef> types;

    /**
     * Create new TypeRef or fetch old TypeRef by the original json data.
     * <p>
     * TODO: Use TypeRef serialize json data to binary.
     *
     * @param node the original json data
     * @return TypeRef
     */
    public TypeRef getType(JsonNode node) {

        return null;
    }

    /**
     * 根据待序列化数据刷新上下文, 主要更新元数据
     *
     * @param data 待序列化的数据
     */
    public void flush(JsonNode data) {
        // null是默认数据类型, 不需要处理
        if (data == null) {
            return;
        }
        this.doCollect(data);
    }

    private TypeRef doCollect(JsonNode node) {
        if (node == null) {
            return null; // NULL
        }
        switch (node.getNodeType()) {
            case NULL:
                return null; // null
            case BOOLEAN:
                return null; // boolean
            case NUMBER:
                return null; // varint or double
            case BINARY:
                return null; // binary
            case STRING:
                return null; // string
            case ARRAY:
                ArrayNode arrayNode = (ArrayNode) node;
                for (Iterator<JsonNode> it = arrayNode.elements(); it.hasNext(); ) {
                    this.doCollect(it.next());
                }
                return null; // array
            case OBJECT:
                ObjectNode objectNode = (ObjectNode) node;
                TypeRef ref = new TypeRef();
                for (Iterator<Map.Entry<String, JsonNode>> it = objectNode.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    String name = entry.getKey();
                    JsonNode val = entry.getValue();
                    TypeRef type = this.doCollect(val);
                    // collect name
                    names.add(name);
                    // collect fields
                    ref.getFields().add(new TypeRef.Field(name, 0));
                }
                types.add(ref);

                return ref; // object
            default:
                throw new IllegalStateException("unsupport data: " + node.getNodeType());
        }
    }

}
