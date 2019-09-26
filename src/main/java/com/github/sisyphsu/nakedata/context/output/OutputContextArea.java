package com.github.sisyphsu.nakedata.context.output;

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
public class OutputContextArea {

    private final DataArea<String> tmpNameArea   = new DataArea<>();
    private final DataArea<int[]>  tmpStructArea = new DataArea<>();
    private final DataArea<Long>   tmpVarintArea = new DataArea<>();
    private final DataArea<Float>  tmpFloatArea  = new DataArea<>();
    private final DataArea<Double> tmpDoubleArea = new DataArea<>();
    private final DataArea<String> tmpStringArea = new DataArea<>();

    private final int                       cxtNameLimit   = 1 << 16;
    private final OutputNameArea            cxtNameArea    = new OutputNameArea();
    private final int                       cxtStructLimit = 1 << 12;
    private final CxtList<String[]>         cxtStructs     = new CxtList<>();
    private final OutputContextPool<int[]>  cxtStructArea  = new OutputContextPool<>();
    private final int                       cxtSymbolLimit = 1 << 16;
    private final OutputContextPool<String> cxtSymbolArea  = new OutputContextPool<>();

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

    /**
     * Register struct for context sharing, could repeat.
     *
     * @param fields One struct's ordered fields
     */
    public void registerStruct(String[] fields) {
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
     *
     * @param symbol Any symbol, meanly Enum.
     */
    public void registerSymbol(String symbol) {
        cxtSymbolArea.add(symbol);
    }

    /**
     * Find the unique ID of the specified struct by its fields.
     *
     * @param fields Fields of struct
     * @return struct's unique id
     */
    public int getStructID(String[] fields) {
        return cxtStructs.getID(fields);
    }

    /**
     * Find the unique ID of the specified symbol.
     *
     * @param symbol String of enum
     * @return symbol's unique id
     */
    public int getSymbolID(String symbol) {
        return cxtSymbolArea.findID(symbol);
    }

}
