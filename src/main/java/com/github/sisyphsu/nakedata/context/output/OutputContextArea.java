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
public final class OutputContextArea {

    private static final int ID_NULL  = 0;
    private static final int ID_FALSE = 1;
    private static final int ID_TRUE  = 2;

    private final boolean enableCxt;

    private final OutputList<String> tmpNameArea   = new OutputList<>();
    private final OutputList<int[]>  tmpStructArea = new OutputList<>();
    private final OutputList<Long>   tmpVarintArea = new OutputList<>();
    private final OutputList<Float>  tmpFloatArea  = new OutputList<>();
    private final OutputList<Double> tmpDoubleArea = new OutputList<>();
    private final OutputList<String> tmpStringArea = new OutputList<>();

    private final int                       cxtNameLimit   = 1 << 16;
    private final OutputContextName         cxtNameArea    = new OutputContextName();
    private final int                       cxtStructLimit = 1 << 12;
    private final OutputList<String[]>      cxtStructs     = new OutputList<>();
    private final OutputContextPool<int[]>  cxtStructArea  = new OutputContextPool<>();
    private final int                       cxtSymbolLimit = 1 << 16;
    private final OutputContextPool<String> cxtSymbolArea  = new OutputContextPool<>();

    public OutputContextArea(boolean enableCxt) {
        this.enableCxt = enableCxt;
    }

    /**
     * Give the context an chance to release name/struct/symbol if too many.
     */
    public void preRelease() {
        cxtNameArea.resetContext();
        cxtStructArea.resetContext();
        cxtSymbolArea.resetContext();
        // try release symbol
        if (cxtSymbolArea.size() > cxtSymbolLimit) {
            cxtSymbolArea.release(cxtSymbolLimit / 10);
        }
        // try release struct
        if (cxtStructArea.size() > cxtStructLimit) {
            long[] releasedItem = cxtStructArea.release(cxtStructLimit / 10);
            for (long item : releasedItem) {
                cxtNameArea.unreference(cxtStructArea.get((int) item));
            }
        }
        // try release name
        while (cxtNameArea.size() > cxtNameLimit) {
            long[] releasedItems = cxtStructArea.release(cxtStructLimit / 10);
            for (long item : releasedItems) {
                cxtNameArea.unreference(cxtStructArea.get((int) item));
            }
        }
    }

    public void addTmpStruct(String[] fields) {
//        tmpStructArea.add();
    }

    public void addTmpVarint(long l) {
        tmpVarintArea.add(l);
    }

    public void addTmpFloat(float f) {
        tmpFloatArea.add(f);
    }

    public void addTmpDouble(double d) {
        tmpDoubleArea.add(d);
    }

    public void addTmpString(String str) {
        tmpStringArea.add(str);
    }

    /**
     * Register struct for context sharing, could repeat.
     */
    public void addCxtStruct(String[] fields) {
        if (cxtStructs.contains(fields)) {
            return;
        }
        int[] nameIds = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            nameIds[i] = cxtNameArea.registerName(fields[i]);
        }
        cxtStructArea.add(nameIds);
    }

    /**
     * Register symbol for context sharing, could repeat.
     */
    public void addCxtSymbol(String symbol) {
        cxtSymbolArea.add(symbol);
    }

    /**
     * Find the unique ID of the specified struct by its fields.
     */
    public int findStructID(String[] fields) {
        if (enableCxt) {

        }
        return cxtStructs.getID(fields);
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
        switch (node.dataType()) {
            case BOOL:
                boolean b = ((BooleanNode) node).value();
                return b ? ID_TRUE : ID_FALSE;

            case VARINT:
                long l = ((VarintNode) node).getValue();
                index = tmpVarintArea.getID(l);
                return index + 3 + cxtSymbolArea.size();

            case FLOAT:
                float f = ((FloatNode) node).getValue();
                index = tmpFloatArea.getID(f);
                return index + 3 + cxtSymbolArea.size() + tmpVarintArea.size();

            case DOUBLE:
                double d = ((DoubleNode) node).getValue();
                index = tmpDoubleArea.getID(d);
                return index + 3 + cxtSymbolArea.size() + tmpVarintArea.size() + tmpFloatArea.size();

            case STRING:
                String str = ((StringNode) node).getValue();
                index = tmpStringArea.getID(str);
                return index + 3 + cxtSymbolArea.size() + tmpVarintArea.size() + tmpFloatArea.size() + tmpDoubleArea.size();

            case SYMBOL:
                String symbol = ((SymbolNode) node).getData();
                if (enableCxt) {
                    return cxtSymbolArea.findID(symbol);
                } else {
                    return tmpStringArea.getID(symbol);
                }
        }
        throw new IllegalArgumentException("The specified String not registered: " + node);
    }

}
