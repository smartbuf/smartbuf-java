package com.github.sisyphsu.nakedata.context.output;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.github.sisyphsu.nakedata.DataType;
import com.github.sisyphsu.nakedata.context.model.ContextVersion;
import com.github.sisyphsu.nakedata.context.model.ContextName;
import com.github.sisyphsu.nakedata.context.model.ContextStruct;
import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.jackson.ObjectNode;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * 输出上下文
 *
 * @author sulin
 * @since 2019-05-01 14:50:15
 */
public class OutputContext {

    private static final Pattern NAME = Pattern.compile("^[A-Za-z_$][\\w$]{0,63}$");

    private long version;
    private ContextVersion log;

    private OutputNamePool namePool;
    private OutputStructPool structPool;
    private OutputTypePool typePool;

    public OutputContext() {
        this.log = new ContextVersion();
        this.namePool = new OutputNamePool(1 << 16);
        this.structPool = new OutputStructPool(1 << 16);
        this.typePool = new OutputTypePool(1 << 16);
    }

    /**
     * 扫描元数据. 数据序列化之前扫描收集"变量名"的增量变化, 用于预处理NamePool以及甄别map与object。
     *
     * @param node 原始数据
     * @return 返回上下文元数据增量版本数据
     */
    public com.github.sisyphsu.nakedata.context.ContextVersion scan(JsonNode node) {
        if (node == null) {
            throw new IllegalStateException("node can't be null");
        }
        // 重置log
        this.log.reset();
        // 开始扫描
        this.doScan(node);
        // 执行垃圾回收
        this.namePool.tryRelease(log);
        this.structPool.tryRelease(log);
        this.typePool.tryRelease(log);
        // 刷新版本号
        log.setVersion((int) ((version++) % Integer.MAX_VALUE));
        return null;
    }

    // 扫描元数据
    private int doScan(JsonNode node) {
        switch (node.getNodeType()) {
            case NULL:
                return DataType.NULL;
            case BOOLEAN:
                return node.booleanValue() ? DataType.TRUE : DataType.FALSE;
            case STRING:
                return DataType.STRING;
            case BINARY:
                return DataType.BINARY;
            case NUMBER:
                if (node.isFloat()) {
                    return DataType.FLOAT;
                } else if (node.isDouble()) {
                    return DataType.DOUBLE;
                } else {
                    return DataType.NUMBER;
                }
            case ARRAY:
                ArrayNode arrayNode = (ArrayNode) node;
                for (Iterator<JsonNode> it = arrayNode.elements(); it.hasNext(); ) {
                    this.scan(it.next());
                }
                return DataType.ARRAY;
            case OBJECT:
                ObjectNode objectNode = (ObjectNode) node;
                boolean isTmp = false;
                Map<String, Integer> fields = new TreeMap<>();
                for (Iterator<Map.Entry<String, JsonNode>> it = objectNode.fields(); it.hasNext(); ) {
                    Map.Entry<String, JsonNode> entry = it.next();
                    isTmp = isTmp || NAME.matcher(entry.getKey()).matches();
                    int type = this.doScan(entry.getValue());// 继续扫描子元素
                    fields.put(entry.getKey(), type);
                }
                if (isTmp) {
                    // TODO 临时类型直接放入临时数组中即可, 但是也需要支持ID复用
//                this.structPool.buildStruct()
//                this.tmpTypes.add(objectNode);
                } else {
                    // 处理上下文类型
                    ContextName[] names = new ContextName[fields.size()];
                    int[] types = new int[fields.size()];
                    int index = 0;
                    for (Map.Entry<String, Integer> entry : fields.entrySet()) {
                        names[index] = namePool.buildName(log, entry.getKey());
                        types[index] = entry.getValue();
                        index++;
                    }
                    ContextStruct struct = structPool.buildStruct(log, names);
                    ContextType type = typePool.buildType(log, struct, types);

                    // 绑定类型ID, 避免输出Body时再次检索所带来的性能损耗
                    objectNode.setType(type);
                }
                return DataType.OBJECT;
            default:
                throw new IllegalArgumentException("Unsupport data: " + node.getNodeType());
        }
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

}
