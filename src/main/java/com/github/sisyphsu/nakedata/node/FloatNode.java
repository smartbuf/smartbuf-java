package com.github.sisyphsu.nakedata.node;

/**
 * @author sulin
 * @since 2019-05-08 21:00:21
 */
public class FloatNode extends AbstractNode {

    public final static FloatNode NULL = new FloatNode();
    public final static FloatNode ZERO = new FloatNode(0);

    private final boolean nil;
    private final float value;

    private FloatNode() {
        this.nil = true;
        this.value = 0;
    }

    private FloatNode(float value) {
        this.nil = false;
        this.value = value;
    }

    public static FloatNode valueOf(float f) {
        if (f == 0) {
            return ZERO;
        }
        return new FloatNode(f);
    }

    public static FloatNode valueOf(Float f) {
        if (f == null) {
            return NULL;
        }
        return valueOf(f.floatValue());
    }

}
