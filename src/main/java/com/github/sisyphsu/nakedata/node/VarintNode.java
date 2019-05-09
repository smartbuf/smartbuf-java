package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.utils.NumberUtils;

/**
 * @author sulin
 * @since 2019-05-08 21:00:46
 */
public class VarintNode extends AbstractNode {

    public final static VarintNode NULL = new VarintNode();

    private final static VarintNode[] TABLE = new VarintNode[256];

    static {
        for (int i = 0; i < TABLE.length; i++) {
            TABLE[i] = new VarintNode(NumberUtils.uintToInt(i));
        }
    }

    private final boolean nil;
    private final long value;

    private VarintNode() {
        this.nil = true;
        this.value = 0;
    }

    private VarintNode(long value) {
        this.nil = false;
        this.value = value;
    }

    public static VarintNode valueOf(byte b) {
        return valueOf((long) b);
    }

    public static VarintNode valueOf(Byte b) {
        return valueOf(b.byteValue());
    }

    public static VarintNode valueOf(short s) {
        return valueOf((long) s);
    }

    public static VarintNode valueOf(Short s) {
        return valueOf(s.shortValue());
    }

    public static VarintNode valueOf(int i) {
        return valueOf((long) i);
    }

    public static VarintNode valueOf(Integer i) {
        return valueOf(i.intValue());
    }

    public static VarintNode valueOf(long l) {
        long ul = NumberUtils.intToUint(l);
        if (ul < TABLE.length) {
            return TABLE[(int) ul];
        }
        return new VarintNode(l);
    }

    public static VarintNode valueOf(Long l) {
        if (l == null)
            return NULL;
        return valueOf(l.longValue());
    }

}
