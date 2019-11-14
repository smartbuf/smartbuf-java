package com.github.smartbuf.node.array;

import com.github.smartbuf.node.Node;
import com.github.smartbuf.node.NodeType;

/**
 * FloatArrayNode represents float[]
 *
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
    public NodeType type() {
        return NodeType.ARRAY_FLOAT;
    }
}
