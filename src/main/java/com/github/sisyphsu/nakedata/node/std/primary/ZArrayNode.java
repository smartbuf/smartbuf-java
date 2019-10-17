package com.github.sisyphsu.nakedata.node.std.primary;

import com.github.sisyphsu.nakedata.NodeType;
import com.github.sisyphsu.nakedata.node.Node;

/**
 * boolean[] array
 *
 * @author sulin
 * @since 2019-06-04 19:53:42
 */
public final class ZArrayNode extends Node {

    private boolean[] items;

    private ZArrayNode(boolean[] items) {
        this.items = items;
    }

    public static ZArrayNode valueOf(boolean[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data can't be null or empty");
        }
        return new ZArrayNode(data);
    }

    @Override
    public boolean[] booleansValue() {
        return items;
    }

    public boolean[] getItems() {
        return items;
    }

    @Override
    public NodeType dataType() {
        return NodeType.N_BOOL_ARRAY;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
