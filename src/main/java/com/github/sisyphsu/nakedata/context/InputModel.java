package com.github.sisyphsu.nakedata.context;

/**
 * @author sulin
 * @since 2019-10-10 21:48:38
 */
public final class InputModel {

    private byte    version;
    private byte    sequence;
    private boolean stream;
    private boolean hasTmpMeta;
    private boolean hasCxtMeta;

    private final Array<Float>  tmpFloats  = new Array<>();
    private final Array<Double> tmpDoubles = new Array<>();
    private final Array<Long>   tmpVarints = new Array<>();
    private final Array<String> tmpStrings = new Array<>();
    private final Array<String> tmpNames   = new Array<>();
    private final Array<int[]>  tmpStructs = new Array<>();

    private final Array<String>  cxtSymbolAdded   = new Array<>();
    private final Array<Integer> cxtSymbolExpired = new Array<>();
    private final Array<String>  cxtNameAdded     = new Array<>();
    private final Array<Integer> cxtNameExpired   = new Array<>();
    private final Array<int[]>   cxtStructAdded   = new Array<>();
    private final Array<Integer> cxtStructExpired = new Array<>();

}
