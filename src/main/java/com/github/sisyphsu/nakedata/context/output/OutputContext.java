package com.github.sisyphsu.nakedata.context.output;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.sisyphsu.nakedata.DataType;
import com.github.sisyphsu.nakedata.context.model.ContextStruct;
import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.context.model.ContextVersion;
import com.github.sisyphsu.nakedata.jackson.node.NArrayNode;
import com.github.sisyphsu.nakedata.jackson.node.NObjectNode;

import java.util.Iterator;
import java.util.Map;
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
    private ContextVersion versionCache;

    private OutputNamePool namePool;
    private OutputStructPool structPool;
    private OutputTypePool typePool;

    public OutputContext() {
        this.versionCache = new ContextVersion();
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
    public ContextVersion scan(JsonNode node) {
        if (node == null) {
            throw new IllegalStateException("node can't be null");
        }
        ContextVersion version = this.versionCache.reset();
        // 执行扫描
        this.doScan(node);
        // 执行垃圾回收
        this.namePool.release(version);
        this.structPool.release(version);
        this.typePool.release(version);
        // 处理版本
        if (version.isEmpty()) {
            version = null;
        } else {
            version.setVersion((int) ((this.version++) % Integer.MAX_VALUE));
        }
        return version;
    }

    // 扫描元数据
    private void doScan(JsonNode node) {
        switch (node.getNodeType()) {
            case ARRAY:
                this.doScanArrayNode((NArrayNode) node);
                break;
            case OBJECT:
                this.doScanObjectNode((NObjectNode) node);
                break;
            case NULL:
            case BOOLEAN:
            case STRING:
            case BINARY:
            case NUMBER:
                break;
            default:
                throw new IllegalArgumentException("Unsupport data: " + node.getNodeType());
        }
    }

    // 扫描并整理Object节点的元数据
    private void doScanObjectNode(NObjectNode node) {
        boolean isTmp = false;
        // 预扫描
        for (Map.Entry<String, JsonNode> entry : node.getFields().entrySet()) {
            isTmp = isTmp || NAME.matcher(entry.getKey()).matches();
            this.doScan(entry.getValue());// 继续扫描子元素
        }
        // 整理元数据
        int[] nameIds = new int[node.size()];
        int[] types = new int[node.size()];
        if (isTmp) {
            // 处理临时元数据
            int offset = 0;
            for (Map.Entry<String, JsonNode> entry : node.getFields().entrySet()) {
                nameIds[offset] = namePool.buildTmpName(versionCache, entry.getKey()).getId();
                types[offset] = getTypeCode(entry.getValue());
                offset++;
            }
            ContextStruct struct = structPool.buildTmpStruct(versionCache, nameIds);
            node.setType(typePool.buildTmpType(versionCache, struct.getId(), types));
        } else {
            // 处理上下文元数据
            int offset = 0;
            for (Map.Entry<String, JsonNode> entry : node.getFields().entrySet()) {
                nameIds[offset] = namePool.buildCxtName(versionCache, entry.getKey()).getId();
                types[offset] = getTypeCode(entry.getValue());
                offset++;
            }
            ContextStruct struct = structPool.buildCxtStruct(versionCache, nameIds);
            node.setType(typePool.buildCxtType(versionCache, struct.getId(), types));
        }
    }

    private void doScanArrayNode(NArrayNode array) {
        NArrayNode.Group group = null;
        for (Iterator<JsonNode> it = array.elements(); it.hasNext(); ) {
            JsonNode node = it.next();
            this.scan(node);
            byte typeCode = getTypeCode(node);
            ContextType type = (node instanceof NObjectNode) ? ((NObjectNode) node).getType() : null;
            if (group != null && group.getType() != type && group.getTypeCode() != typeCode) {
                group.setEnd(false);
                group.setCount(group.getCount() + 1);
                group = null;
            }
            if (group == null) {
                group = new NArrayNode.Group();
                group.setType(type);
                group.setTypeCode(typeCode);
                group.setEnd(true);
                array.getGroups().add(group);
            }
            group.setCount(group.getCount() + 1);
        }
    }

    // 获取指定node的标准类型代码
    private byte getTypeCode(JsonNode node) {
        if (node instanceof DataType) {
            return ((DataType) node).getTypeCode();
        } else {
            return DataType.NULL;
        }
    }

}
