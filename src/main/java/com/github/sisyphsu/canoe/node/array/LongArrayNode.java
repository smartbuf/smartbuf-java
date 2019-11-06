package com.github.sisyphsu.canoe.node.array;

import com.github.sisyphsu.canoe.node.Node;

/**
 * @author sulin
 * @since 2019-11-03 14:47:09
 */
public final class LongArrayNode extends Node {

    private final long[] data;

    public LongArrayNode(long[] data) {
        this.data = data;
    }

    @Override
    public Object value() {
        return data;
    }

    @Override
    public Type type() {
        return Type.ARRAY_LONG;
    }
}
