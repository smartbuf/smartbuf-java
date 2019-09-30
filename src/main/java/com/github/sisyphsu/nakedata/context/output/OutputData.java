package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.*;
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

    public static final byte ID_NULL  = 0x00;
    public static final byte ID_FALSE = 0x01;
    public static final byte ID_TRUE  = 0x02;

    private final boolean enableCxt;

    private final int cxtSymbolLimit = 1 << 16;

    final VarintArray   varintArea = new VarintArray(true);
    final FloatArray    floatArea  = new FloatArray(true);
    final DoubleArray   doubleArea = new DoubleArray(true);
    final Array<String> stringArea = new Array<>(true);

    final RecycleArray<String> symbolArea    = new RecycleArray<>();
    final Array<String>        symbolAdded   = new Array<>(true);
    final VarintArray          symbolExpired = new VarintArray(true);

    public OutputData(boolean enableCxt) {
        this.enableCxt = enableCxt;
    }

    public void clear() {
        this.varintArea.clear();
        this.floatArea.clear();
        this.doubleArea.clear();
        this.stringArea.clear();
        if (!enableCxt) {
            return;
        }
        this.symbolAdded.clear();
        this.symbolExpired.clear();
        if (symbolArea.size() >= cxtSymbolLimit) {
            long[] heap = symbolArea.release(cxtSymbolLimit / 10);
            for (long l : heap) {
                symbolExpired.add((int) l);
            }
        }
    }

    public int findFloatID(float f) {
        return 0;
    }

    public int findDoubleID(double d) {
        return 0;
    }

    public int findVarintID(long v) {
        return 0;
    }

    public int findStringID(String str) {
        return 0;
    }

    public int findSymbolID(String symbol) {
        return 0;
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
                index = varintArea.offset(l);
                return index + 3 + symbolCount;

            case FLOAT:
                float f = ((FloatNode) node).getValue();
                index = floatArea.offset(f);
                return index + 3 + symbolCount + varintArea.size();

            case DOUBLE:
                double d = ((DoubleNode) node).getValue();
                index = doubleArea.offset(d);
                return index + 3 + symbolCount + varintArea.size() + floatArea.size();

            case STRING:
                String str = ((StringNode) node).getValue();
                index = stringArea.offset(str);
                return index + 3 + symbolCount + varintArea.size() + floatArea.size() + doubleArea.size();

            case SYMBOL:
                String symbol = ((SymbolNode) node).getData();
                if (enableCxt) {
                    return symbolArea.offset(symbol);
                } else {
                    return stringArea.offset(symbol);
                }
        }
        throw new IllegalArgumentException("The specified String not registered: " + node);
    }

}
