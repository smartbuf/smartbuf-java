package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.std.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 报文的Data区模型。
 * 对于Input和Output，数据区的处理逻辑应该有很大的差异。
 * <p>
 * 在扫描阶段，直接准备四个List即可？
 *
 * @author sulin
 * @since 2019-09-24 20:11:22
 */
public final class OutputDataPool {

    private static final int ID_NULL = 0;
    private static final int ID_FALSE = 1;
    private static final int ID_TRUE = 2;

    private int symbolCount = 0;

    private int varintCount = 0;
    private Map<Long, Integer> varintIndex = new HashMap<>();

    private int floatCount = 0;
    private Map<Float, Integer> floatIndex = new HashMap<>();

    private int doubleCount = 0;
    private Map<Double, Integer> doubleIndex = new HashMap<>();

    private int stringCount = 0;
    private Map<String, Integer> stringIndex = new HashMap<>();

    /**
     * 向数据区注册varint，支持byte、short、int、long
     */
    public void registerVarint(long varint) {
        if (varintIndex.containsKey(varint))
            return;
        varintIndex.put(varint, varintCount++);
    }

    /**
     * 向数据区注册float
     */
    public void registerFloat(float f) {
        if (floatIndex.containsKey(f)) {
            return;
        }
        floatIndex.put(f, floatCount++);
    }

    /**
     * 向数据区注册double
     */
    public void registerDouble(double d) {
        if (doubleIndex.containsKey(d)) {
            return;
        }
        doubleIndex.put(d, doubleCount++);
    }

    /**
     * 向数据区注册string
     */
    public void registerString(String str) {
        if (stringIndex.containsKey(str)) {
            return;
        }
        stringIndex.put(str, stringCount++);
    }

    /**
     * 获取指定数据节点在数据区的唯一ID
     */
    public int getDataID(Node node) {
        if (node == null || node.isNull()) {
            return ID_NULL;
        }
        Integer index;
        switch (node.dataType()) {
            case BOOL:
                Boolean b = ((BooleanNode) node).value();
                if (b == null) {
                    return ID_NULL;
                }
                return b ? ID_TRUE : ID_FALSE;

            case VARINT:
                Long l = ((VarintNode) node).getValue();
                if (l == null) {
                    return ID_NULL;
                }
                index = varintIndex.get(l);
                if (index == null) {
                    throw new IllegalArgumentException("The specified Long not registered: " + l);
                }
                return index + 3 + symbolCount;

            case FLOAT:
                Float f = ((FloatNode) node).getValue();
                if (f == null) {
                    return ID_NULL;
                }
                index = floatIndex.get(f);
                if (index == null) {
                    throw new IllegalArgumentException("The specified Float not registered: " + f);
                }
                return index + 3 + symbolCount + varintCount;

            case DOUBLE:
                Double d = ((DoubleNode) node).getValue();
                if (d == null) {
                    return ID_NULL;
                }
                index = doubleIndex.get(d);
                if (index == null) {
                    throw new IllegalArgumentException("The specified Double not registered: " + d);
                }
                return index + 3 + symbolCount + varintCount + floatCount;

            case STRING:
                String str = ((StringNode) node).getValue();
                if (str == null) {
                    return ID_NULL;
                }
                index = stringIndex.get(str);
                if (index == null) {
                    throw new IllegalArgumentException("The specified String not registered: " + str);
                }
                return index + 3 + symbolCount + varintCount + floatCount + doubleCount;
        }

        throw new IllegalArgumentException("The specified String not registered: " + node);
    }

}
