package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.std.*;

/**
 * 上下文复用的数据区，封装name(String)、struct(int[])、symbol(String)的上下文管理
 * <p>
 * struct可以简单的通过activeTime判断活跃度。
 * <p>
 * name受到struct的引用，需要特殊的方式处理活跃度和GC策略。
 * name只受struct影响，它需要记录一个refCount，
 * 释放上下文时，通过释放struct来更新refCount，然后再释放全部refCount为0的name。
 * <p>
 * symbol相对就简单了许多，只需要通过activeTime记录即可。
 *
 * @author sulin
 * @since 2019-09-26 11:38:33
 */
public final class OutputData {

    private static final int ID_NULL  = 0;
    private static final int ID_FALSE = 1;
    private static final int ID_TRUE  = 2;

    private final boolean enableCxt;

    private final OutputList<Long>   varintArea = new OutputList<>();
    private final OutputList<Float>  floatArea  = new OutputList<>();
    private final OutputList<Double> doubleArea = new OutputList<>();
    private final OutputList<String> stringArea = new OutputList<>();

    private final int                cxtSymbolLimit = 1 << 16;
    private final OutputPool<String> symbolArea     = new OutputPool<>();

    public OutputData(boolean enableCxt) {
        this.enableCxt = enableCxt;
    }

    public void preRelease() {
        symbolArea.resetContext();
        // try release symbol
        if (symbolArea.size() > cxtSymbolLimit) {
            symbolArea.release(cxtSymbolLimit / 10);
        }
    }

    /**
     * Register the specified data node into this area.
     */
    public void addData(Node node) {
        switch (node.dataType()) {
            case NULL:
            case BOOL:
                break;
            case FLOAT:
                floatArea.add(((FloatNode) node).getValue());
                break;
            case DOUBLE:
                doubleArea.add(((DoubleNode) node).getValue());
                break;
            case VARINT:
                varintArea.add(((VarintNode) node).getValue());
                break;
            case STRING:
                stringArea.add(((StringNode) node).getValue());
                break;
            case SYMBOL:
                String symbol = ((SymbolNode) node).getData();
                if (enableCxt) {
                    symbolArea.add(symbol);
                } else {
                    stringArea.add(symbol);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupport data: " + node);
        }
    }

    /**
     * Find the unique id of the specified data node.
     */
    @SuppressWarnings("ConstantConditions")
    public int findDataID(Node node) {
        if (node == null || node.isNull()) {
            return ID_NULL;
        }
        int index;
        int symbolCount = symbolArea.size();
        switch (node.dataType()) {
            case BOOL:
                boolean b = ((BooleanNode) node).value();
                return b ? ID_TRUE : ID_FALSE;

            case VARINT:
                long l = ((VarintNode) node).getValue();
                index = varintArea.getID(l);
                return index + 3 + symbolCount;

            case FLOAT:
                float f = ((FloatNode) node).getValue();
                index = floatArea.getID(f);
                return index + 3 + symbolCount + varintArea.size();

            case DOUBLE:
                double d = ((DoubleNode) node).getValue();
                index = doubleArea.getID(d);
                return index + 3 + symbolCount + varintArea.size() + floatArea.size();

            case STRING:
                String str = ((StringNode) node).getValue();
                index = stringArea.getID(str);
                return index + 3 + symbolCount + varintArea.size() + floatArea.size() + doubleArea.size();

            case SYMBOL:
                String symbol = ((SymbolNode) node).getData();
                if (enableCxt) {
                    return symbolArea.findID(symbol);
                } else {
                    return stringArea.getID(symbol);
                }
        }
        throw new IllegalArgumentException("The specified String not registered: " + node);
    }

}
