package com.github.sisyphsu.nakedata.context.output;

/**
 * 需要为ObjectNode提供一种非常便利的struct-fields-id映射关系。
 *
 * @author sulin
 * @since 2019-09-26 21:32:54
 */
public final class OutputSchema {

    private final boolean enableCxt;

    private final OutputList<String> tmpNameArea   = new OutputList<>();
    private final OutputList<int[]>  tmpStructArea = new OutputList<>();

    private final int               cxtNameLimit = 1 << 16;
    private final OutputContextName cxtNameArea  = new OutputContextName();

    private final int                      cxtStructLimit = 1 << 12;
    private final OutputList<String[]>     cxtStructs     = new OutputList<>();
    private final OutputContextPool<int[]> cxtStructArea  = new OutputContextPool<>();

    private final int                       cxtSymbolLimit = 1 << 16;
    private final OutputContextPool<String> cxtSymbolArea  = new OutputContextPool<>();

    public OutputSchema(boolean enableCxt) {
        this.enableCxt = enableCxt;
    }

    /**
     * Add an struct into schema.
     */
    public void addTmpStruct(String[] fields) {
        // tmpStructArea.add();
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

    public int findStructID(String[] fields) {
        return 0;
    }

}
