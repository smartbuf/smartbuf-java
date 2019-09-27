package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.model.ContextVersion;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.MixArrayNode;
import com.github.sisyphsu.nakedata.node.std.ObjectNode;

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

    private long           version;
    private boolean        streamMode;
    private ContextVersion versionCache;

    private OutputMeta meta = new OutputMeta();
    private OutputData data = new OutputData(false);

    public OutputContext() {
        this.versionCache = new ContextVersion();
    }

    /**
     * 扫描元数据. 数据序列化之前扫描收集"变量名"的增量变化, 用于预处理NamePool以及甄别map与object。
     *
     * @param node 原始数据
     * @return 返回上下文元数据增量版本数据
     */
    public ContextVersion scan(Node node) {
        if (node == null) {
            throw new IllegalArgumentException("node can't be null");
        }
        ContextVersion version = this.versionCache.reset();
        // 预先执行垃圾回收
        meta.preRelease();
        // 执行扫描
        this.doScan(node);
        // 处理版本
        if (version.isEmpty()) {
            version = null;
        } else {
            version.setVersion((int) ((this.version++) % Integer.MAX_VALUE));
        }
        return version;
    }

    // 扫描元数据
    private void doScan(Node node) {
        if (node.isNull()) {
            return;
        }
        switch (node.dataType()) {
            case ARRAY:
                this.doScanArrayNode((MixArrayNode) node);
                break;
            case OBJECT:
                this.doScanObjectNode((ObjectNode) node);
                break;
            default:
                data.addData(node);
                break;
        }
    }

    // 扫描并整理Object节点的元数据
    private void doScanObjectNode(ObjectNode node) {
        boolean isTmp = false;
        // 预扫描
        for (Map.Entry<String, Node> entry : node.getFields().entrySet()) {
            isTmp = isTmp || NAME.matcher(entry.getKey()).matches();
            this.doScan(entry.getValue());// 继续扫描子元素
        }
        // 整理元数据
        int[] nameIds = new int[node.size()];
        int[] types = new int[node.size()];
        if (isTmp) {
            // 处理临时元数据
            int offset = 0;
            for (Map.Entry<String, Node> entry : node.getFields().entrySet()) {
//                nameIds[offset] = namePool.buildTmpName(versionCache, entry.getKey()).getId();
                types[offset] = entry.getValue().dataType().getCode();
                offset++;
            }
//            ContextStruct struct = structPool.buildTmpStruct(versionCache, nameIds);
//            node.setContextType(typePool.buildTmpType(versionCache, struct.getId(), types));
        } else {
            // 处理上下文元数据
            int offset = 0;
            for (Map.Entry<String, Node> entry : node.getFields().entrySet()) {
//                nameIds[offset] = namePool.buildCxtName(versionCache, entry.getKey()).getId();
                types[offset] = entry.getValue().dataType().getCode();
                offset++;
            }
//            ContextStruct struct = structPool.buildCxtStruct(versionCache, nameIds);
//            node.setContextType(typePool.buildCxtType(versionCache, struct.getId(), types));
        }
    }

    private void doScanArrayNode(MixArrayNode array) {
//        MixArrayNode.Group group = null;
//        for (Node node : array.getItems()) {
//            this.scan(node);
//            byte typeCode = node.getDataType().getCode();
//            ContextType type = node.getContextType();
//            if (group != null && group.getType() != type && group.getTypeCode() != typeCode) {
//                group.setEnd(false);
//                group.setCount(group.getCount() + 1);
//                group = null;
//            }
//            if (group == null) {
//                group = new MixArrayNode.Group();
//                group.setType(type);
//                group.setTypeCode(typeCode);
//                group.setEnd(true);
//                array.getGroups().add(group);
//            }
//            group.setCount(group.getCount() + 1);
//        }
    }

}
