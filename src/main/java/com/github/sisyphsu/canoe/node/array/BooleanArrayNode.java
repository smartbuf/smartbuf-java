package com.github.sisyphsu.canoe.node.array;

import com.github.sisyphsu.canoe.node.Node;

/**
 * @author sulin
 * @since 2019-11-03 14:46:34
 */
public final class BooleanArrayNode extends Node {

    private final boolean[] data;

    public BooleanArrayNode(boolean[] data) {
        this.data = data;
    }

    @Override
    public Object value() {
        return data;
    }
}
