package com.github.sisyphsu.nakedata.node.std.primary;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.NodeType;

/**
 * int[] array
 *
 * @author sulin
 * @since 2019-06-05 15:54:27
 */
public final class IArrayNode extends Node {

    private int[] items;

    private IArrayNode(int[] items) {
        this.items = items;
    }

    public static IArrayNode valueOf(int[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data can't be null or empty");
        }
        return new IArrayNode(data);
    }

    @Override
    public int[] intsValue() {
        return items;
    }

    @Override
    public NodeType dataType() {
        return NodeType.N_INT_ARRAY;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
