package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.model.Frame;
import com.github.sisyphsu.nakedata.context.model.FrameMeta;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.node.array.MixArrayNode;
import com.github.sisyphsu.nakedata.node.std.*;

import java.util.Map;
import java.util.regex.Pattern;

/**
 * 输出构造器，负责扫描源数据，序列化输出报文
 *
 * @author sulin
 * @since 2019-05-01 14:50:15
 */
public class OutputBuilder {

    private static final Pattern NAME = Pattern.compile("^[A-Za-z_$][\\w$]{0,63}$");

    private long      version;
    private boolean   enableCxt;
    private FrameMeta versionCache;

    private final OutputSchema meta = new OutputSchema(false);
    private final OutputData   data = new OutputData(false);

    public OutputBuilder() {
        this.versionCache = new FrameMeta();
    }

    public Frame buildOutput(Node node) {
        if (node == null) {
            throw new NullPointerException("node can't be null");
        }
        // step1. 构建FrameMeta
        FrameMeta meta = this.scan(node);

        // step2. 构建OutputFrame

        return null;
    }

    /**
     * 扫描元数据. 数据序列化之前扫描收集"变量名"的增量变化, 用于预处理NamePool以及甄别map与object。
     */
    private FrameMeta scan(Node node) {
        data.clear();
        if (enableCxt) {
            meta.clear();
        }
        // 执行扫描
        this.doScan(node);

        FrameMeta version = new FrameMeta();
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
            case NULL:
            case BOOL:
                break;
            case FLOAT:
                data.floatArea.add(((FloatNode) node).getValue());
                break;
            case DOUBLE:
                data.doubleArea.add(((DoubleNode) node).getValue());
                break;
            case VARINT:
                data.varintArea.add(((VarintNode) node).getValue());
                break;
            case STRING:
                data.stringArea.add(((StringNode) node).getValue());
                break;
            case SYMBOL:
                String symbol = ((SymbolNode) node).getData();
                if (enableCxt) {
                    data.symbolArea.add(symbol);
                } else {
                    data.stringArea.add(symbol);
                }
                break;
            case ARRAY:
                this.doScanArrayNode((ArrayNode) node);
                break;
            case OBJECT:
                this.doScanObjectNode((ObjectNode) node);
                break;
            default:
                throw new IllegalArgumentException("Unsupport data: " + node);
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
                    data.stringArea.add((String) item);
                }
                break;
            case SYMBOL:
                for (Object item : array.getItems()) {
                    if (enableCxt) {
                        data.symbolArea.add(String.valueOf(item));
                    } else {
                        data.stringArea.add(String.valueOf(item));
                    }
                }
                break;
            case OBJECT:
                for (Object item : array.getItems()) {
                    this.doScanObjectNode((ObjectNode) item);
                }
                break;
        }
    }

    /**
     * 扫描并整理Object节点的元数据
     */
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

}
