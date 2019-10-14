package com.github.sisyphsu.nakedata.context;

/**
 * @author sulin
 * @since 2019-10-14 11:00:08
 */
public final class InputPool {

    private final Schema schema;

    private final Array<Float>    tmpFloats;
    private final Array<Double>   tmpDoubles;
    private final Array<Long>     tmpVarints;
    private final Array<String>   tmpStrings;
    private final Array<String>   tmpNames;
    private final Array<String[]> tmpStructs = new Array<>();

    private final IDAllocator   cxtNameId = new IDAllocator();
    private final Array<String> cxtNames  = new Array<>();

    private final IDAllocator     cxtStructID = new IDAllocator();
    private final Array<String[]> cxtStructs  = new Array<>();

    private final IDAllocator   cxtSymbolID = new IDAllocator();
    private final Array<String> cxtSymbols  = new Array<>();

    public InputPool(Schema schema) {
        this.schema = schema;
        this.tmpFloats = schema.tmpFloats;
        this.tmpDoubles = schema.tmpDoubles;
        this.tmpVarints = schema.tmpVarints;
        this.tmpStrings = schema.tmpStrings;
        this.tmpNames = schema.tmpNames;
    }

    public void sync() {
        this.tmpStructs.clear();
    }

}
