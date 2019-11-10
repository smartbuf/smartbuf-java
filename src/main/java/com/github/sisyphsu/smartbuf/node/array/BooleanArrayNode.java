package com.github.sisyphsu.smartbuf.node.array;

import com.github.sisyphsu.smartbuf.node.Node;
import com.github.sisyphsu.smartbuf.node.NodeType;

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

    @Override
    public NodeType type() {
        return NodeType.ARRAY_BOOLEAN;
    }
}
