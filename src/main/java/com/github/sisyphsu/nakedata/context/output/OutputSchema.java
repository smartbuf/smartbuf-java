package com.github.sisyphsu.nakedata.context.output;

/**
 * @author sulin
 * @since 2019-09-26 21:32:54
 */
public class OutputSchema {

    private final OutputList<String> tmpNameArea   = new OutputList<>();
    private final OutputList<int[]>  tmpStructArea = new OutputList<>();

    private final int               cxtNameLimit = 1 << 16;
    private final OutputContextName cxtNameArea  = new OutputContextName();

    private final int                      cxtStructLimit = 1 << 12;
    private final OutputList<String[]>     cxtStructs     = new OutputList<>();
    private final OutputContextPool<int[]> cxtStructArea  = new OutputContextPool<>();

    private final int                       cxtSymbolLimit = 1 << 16;
    private final OutputContextPool<String> cxtSymbolArea  = new OutputContextPool<>();

}
