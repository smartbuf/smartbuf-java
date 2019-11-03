package com.github.sisyphsu.canoe.node.array;

import com.github.sisyphsu.canoe.node.Node;

/**
 * @author sulin
 * @since 2019-11-03 14:47:16
 */
public final class FloatArrayNode extends Node {

    private final float[] data;

    public FloatArrayNode(float[] data) {
        this.data = data;
    }

    @Override
    public Object value() {
        return data;
    }

    @Override
    public float[] floatArrayValue() {
        return data;
    }
}
