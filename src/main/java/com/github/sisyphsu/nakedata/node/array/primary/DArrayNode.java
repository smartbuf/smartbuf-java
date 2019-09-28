package com.github.sisyphsu.nakedata.node.array.primary;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.DataType;

/**
 * double[] array
 *
 * @author sulin
 * @since 2019-06-04 16:51:04
 */
public final class DArrayNode extends Node {

    private double[] items;

    private DArrayNode(double[] items) {
        this.items = items;
    }

    public static DArrayNode valueOf(double[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data can't be null or empty");
        }
        return new DArrayNode(data);
    }

    public double[] getItems() {
        return items;
    }

    @Override
    public DataType dataType() {
        return DataType.N_DOUBLE_ARRAY;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
