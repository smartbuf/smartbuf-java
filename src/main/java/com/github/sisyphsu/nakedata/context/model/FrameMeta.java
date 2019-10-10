package com.github.sisyphsu.nakedata.context.model;

import com.github.sisyphsu.nakedata.context.common.Array;
import com.github.sisyphsu.nakedata.context.common.DoubleArray;
import com.github.sisyphsu.nakedata.context.common.FloatArray;
import com.github.sisyphsu.nakedata.context.common.LongArray;
import lombok.Getter;

/**
 * 报文元数据，在数据包的头部声明报文体的数据模型、数据区等
 *
 * @author sulin
 * @since 2019-05-03 17:43:58
 */
@Getter
@Deprecated
public final class FrameMeta {

    private static final byte VERSION     = (byte) 0b0100_0000;
    private static final byte FLAG_STREAM = (byte) 0b0010_0000;
    private static final byte FLAG_HEAD   = (byte) 0b0001_0000;

    private static final byte CODE_FLOAT          = (byte) 1;
    private static final byte CODE_DOUBLE         = (byte) 2;
    private static final byte CODE_VARINT         = (byte) 3;
    private static final byte CODE_STRING         = (byte) 4;
    private static final byte CODE_NAMES          = (byte) 5;
    private static final byte CODE_STRUCTS        = (byte) 6;
    private static final byte CODE_NAME_ADDED     = (byte) 7;
    private static final byte CODE_NAME_EXPIRED   = (byte) 8;
    private static final byte CODE_STRUCT_ADDED   = (byte) 9;
    private static final byte CODE_STRUCT_EXPIRED = (byte) 10;
    private static final byte CODE_SYMBOL_ADDED   = (byte) 11;
    private static final byte CODE_SYMBOL_EXPIRED = (byte) 12;

    private final boolean output;
    private final boolean enableCxt;

    private final FloatArray    tmpFloatArea;
    private final DoubleArray   tmpDoubleArea;
    private final LongArray     tmpVarintArea;
    private final Array<String> tmpStringArea;

    private final Array<String> tmpNames;
    private final Array<int[]>  tmpStructs;

    private final Array<String> cxtNameAdded;
    private final Array<int[]>  cxtStructAdded;
    private final Array<String> cxtSymbolAdded;

    private final LongArray cxtNameExpired;
    private final LongArray cxtStructExpired;
    private final LongArray cxtSymbolExpired;

    private int version;

    /**
     * Initialize frame's metadata, it should be reused for every context.
     *
     * @param output Used for output side or not
     */
    public FrameMeta(boolean output, boolean enableCxt) {
        this.output = output;
        this.enableCxt = enableCxt;
        this.tmpFloatArea = new FloatArray(output);
        this.tmpDoubleArea = new DoubleArray(output);
        this.tmpVarintArea = new LongArray(output);
        this.tmpStringArea = new Array<>(output);
        this.tmpNames = new Array<>(output);
        this.tmpStructs = new Array<>(output);
        this.cxtNameAdded = new Array<>(output);
        this.cxtStructAdded = new Array<>(output);
        this.cxtSymbolAdded = new Array<>(output);
        this.cxtNameExpired = new LongArray(output);
        this.cxtStructExpired = new LongArray(output);
        this.cxtSymbolExpired = new LongArray(output);
    }

}
