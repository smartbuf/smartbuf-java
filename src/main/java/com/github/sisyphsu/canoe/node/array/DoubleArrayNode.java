package com.github.sisyphsu.canoe.node.array;

import com.github.sisyphsu.canoe.node.Node;

/**
 * @author sulin
 * @since 2019-11-03 14:47:29
 */
public final class DoubleArrayNode extends Node {

    private final double[] data;

    public DoubleArrayNode(double[] data) {
        this.data = data;
    }

    @Override
    public Object value() {
        return data;
    }

    @Override
    public double[] doubleArrayValue() {
        return data;
    }
}
