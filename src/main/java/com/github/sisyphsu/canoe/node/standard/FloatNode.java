package com.github.sisyphsu.canoe.node.standard;

import com.github.sisyphsu.canoe.node.Node;

/**
 * FloatNode represents float and Float.
 *
 * @author sulin
 * @since 2019-05-08 21:00:21
 */
public final class FloatNode extends Node {

    public final static FloatNode ZERO = new FloatNode(0);

    private final float value;

    private FloatNode(float value) {
        this.value = value;
    }

    public static FloatNode valueOf(float f) {
        if (f == 0) {
            return ZERO;
        }
        return new FloatNode(f);
    }

    public static FloatNode valueOf(Float f) {
        return valueOf(f.floatValue());
    }

    @Override
    public Object value() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

}
