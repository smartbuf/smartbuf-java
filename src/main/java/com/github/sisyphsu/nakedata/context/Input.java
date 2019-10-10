package com.github.sisyphsu.nakedata.context;

/**
 * @author sulin
 * @since 2019-10-10 21:43:41
 */
public final class Input {

    private final Array<String>   cxtSymbols = new Array<>();
    private final Array<String>   cxtNames   = new Array<>();
    private final IDAllocator     cxtIdAlloc = new IDAllocator();
    private final Array<String[]> cxtStructs = new Array<>();

    public Object findDataByID(int dataId) {
        return null;
    }

    public String findNameByID(int nameId) {
        return null;
    }

    public String[] findStructByID(int structID) {
        return null;
    }

}
