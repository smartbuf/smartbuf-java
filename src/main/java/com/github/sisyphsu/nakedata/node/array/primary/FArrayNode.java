package com.github.sisyphsu.nakedata.node.array.primary;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.DataType;

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

    public float[] getItems() {
        return items;
    }

    @Override
    public DataType dataType() {
        return DataType.N_FLOAT_ARRAY;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
