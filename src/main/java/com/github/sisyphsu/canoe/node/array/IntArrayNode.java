package com.github.sisyphsu.canoe.node.array;

import com.github.sisyphsu.canoe.Const;
import com.github.sisyphsu.canoe.node.Node;

/**
 * @author sulin
 * @since 2019-11-03 14:46:58
 */
public final class IntArrayNode extends Node {

    private final int[] data;

    public IntArrayNode(int[] data) {
        this.data = data;
    }

    @Override
    public Object value() {
        return data;
    }

    @Override
    public byte type() {
        return Const.TYPE_NARRAY_INT;
    }
}
