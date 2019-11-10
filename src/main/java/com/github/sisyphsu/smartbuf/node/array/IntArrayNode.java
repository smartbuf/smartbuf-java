package com.github.sisyphsu.smartbuf.node.array;

import com.github.sisyphsu.smartbuf.node.Node;
import com.github.sisyphsu.smartbuf.node.NodeType;

/**
 * IntArrayNode represents int[]
 *
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
    public NodeType type() {
        return NodeType.ARRAY_INT;
    }
}
