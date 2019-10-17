package com.github.sisyphsu.nakedata.node.std.primary;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.NodeType;

/**
 * float[] array
 *
 * @author sulin
 * @since 2019-06-04 16:34:25
 */
public final class FArrayNode extends Node {

    private float[] items;

    private FArrayNode(float[] items) {
        this.items = items;
    }

    public static FArrayNode valueOf(float[] items) {
        if (items == null || items.length == 0) {
            throw new IllegalArgumentException("items can't be null or empty");
        }
        return new FArrayNode(items);
    }

    @Override
    public float[] floatsValue() {
        return items;
    }

    @Override
    public NodeType dataType() {
        return NodeType.N_FLOAT_ARRAY;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
