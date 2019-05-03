package com.github.sisyphsu.nakedata.context.output;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.sisyphsu.nakedata.context.ContextVersion;

import java.util.*;
import java.util.regex.Pattern;

/**
 * 输出上下文
 *
 * @author sulin
 * @since 2019-05-01 14:50:15
 */
public class OutputContext {

    private static final Pattern NAME = Pattern.compile("^[A-Za-z_$][\\w$]{0,63}$");

    /**
     * 扫描到的持久变量名, 元数据扫描阶段使用, 支持复用以提高性能
     */
    private Set<String> cxtNames = new HashSet<>();
    /**
     * 扫描到的临时变量名
     */
    private Set<String> tmpNames = new HashSet<>();
    /**
     * 扫描到的持久数据类型
     */
    private Set<ObjectNode> cxtTypes = new HashSet<>();
    /**
     * 扫描到的临时数据类型
     */
    private Set<ObjectNode> tmpTypes = new HashSet<>();

    private OutputNamePool namePool;

    /**
     * 预扫描元数据. 数据序列化之前扫描收集"变量名"的增量变化, 用于预处理NamePool以及甄别map与object。
     *
     * @param node 原始数据
     * @return 返回上下文元数据增量版本数据
     */
    public ContextVersion preScan(JsonNode node) {
        if (node == null) {
            return null;
        }
        if (node.getNodeType() == JsonNodeType.ARRAY) {
            ArrayNode arrayNode = (ArrayNode) node;
            for (Iterator<JsonNode> it = arrayNode.elements(); it.hasNext(); ) {
                this.preScan(it.next());
            }
        } else if (node.getNodeType() == JsonNodeType.OBJECT) {
            ObjectNode objectNode = (ObjectNode) node;
            boolean isTmp = false;
            List<String> names = new ArrayList<>();
            for (Iterator<Map.Entry<String, JsonNode>> it = objectNode.fields(); it.hasNext(); ) {
                Map.Entry<String, JsonNode> entry = it.next();
                isTmp = isTmp || NAME.matcher(entry.getKey()).matches();
                names.add(entry.getKey());
                // 继续扫描子元素
                this.preScan(entry.getValue());
            }
            if (isTmp) {
                // 标记为临时类型
                this.tmpNames.addAll(names);
                this.tmpTypes.add(objectNode);
            } else {
                // 标记为持久类型
                this.cxtNames.addAll(names);
                this.cxtTypes.add(objectNode);
            }
        }
        return null;
    }

    /**
     * Create new TypeRef or fetch old TypeRef by the original json data.
     * <p>
     * TODO: Use TypeRef serialize json data to binary.
     *
     * @param node the original json data
     * @return TypeRef
     */
    public ActiveRecord getType(JsonNode node) {

        return null;
    }

    /**
     * 获取指定JsonNode的type-id
     *
     * @param node 原始JsonNode
     * @return 类型ID
     */
    public int getTypeId(JsonNode node) {
        return 0;
    }

//    /**
//     * 根据待序列化数据刷新上下文, 主要更新元数据
//     *
//     * @param data 待序列化的数据
//     */
//    public void flush(JsonNode data) {
//        // null是默认数据类型, 不需要处理
//        if (data == null) {
//            return;
//        }
//        this.doCollect(data);
//    }

//    private TypeRef doCollect(JsonNode node) {
//        if (node == null) {
//            return null; // NULL
//        }
//        switch (node.getNodeType()) {
//            case NULL:
//                return null; // null
//            case BOOLEAN:
//                return null; // boolean
//            case NUMBER:
//                return null; // varint or double
//            case BINARY:
//                return null; // binary
//            case STRING:
//                return null; // string
//            case ARRAY:
//                ArrayNode arrayNode = (ArrayNode) node;
//                for (Iterator<JsonNode> it = arrayNode.elements(); it.hasNext(); ) {
//                    this.doCollect(it.next());
//                }
//                return null; // array
//            case OBJECT:
//                ObjectNode objectNode = (ObjectNode) node;
//                TypeRef ref = new TypeRef();
//                for (Iterator<Map.Entry<String, JsonNode>> it = objectNode.fields(); it.hasNext(); ) {
//                    Map.Entry<String, JsonNode> entry = it.next();
//                    String name = entry.getKey();
//                    JsonNode val = entry.getValue();
//                    TypeRef type = this.doCollect(val);
//                    // collect name
//                    cxtNames.add(name);
//                    // collect fields
//                    ref.getFields().add(new TypeRef.Field(name, 0));
//                }
////                types.add(ref);
//
//                return ref; // object
//            default:
//                throw new IllegalStateException("unsupport data: " + node.getNodeType());
//        }
//    }

}
