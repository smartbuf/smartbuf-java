package com.github.sisyphsu.nakedata.node.array.primary;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.DataType;

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

    public int[] getItems() {
        return items;
    }

    @Override
    public DataType dataType() {
        return null;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
