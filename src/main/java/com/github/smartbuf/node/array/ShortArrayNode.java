package com.github.smartbuf.node.array;

import com.github.smartbuf.node.Node;
import com.github.smartbuf.node.NodeType;

/**
 * ShortArrayNode represents short[]
 *
 * @author sulin
 * @since 2019-11-03 14:46:51
 */
public final class ShortArrayNode extends Node {

    private final short[] data;

    public ShortArrayNode(short[] data) {
        this.data = data;
    }

    @Override
    public Object value() {
        return data;
    }

    @Override
    public NodeType type() {
        return NodeType.ARRAY_SHORT;
    }
}
