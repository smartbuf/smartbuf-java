package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * double[] array
 *
 * @author sulin
 * @since 2019-06-04 16:51:04
 */
public class DoubleArrayNode extends ArrayNode {

    public static final DoubleArrayNode NULL = new DoubleArrayNode(null);
    public static final DoubleArrayNode EMPTY = new DoubleArrayNode(new double[0]);

    private double[] items;

    private DoubleArrayNode(double[] items) {
        this.items = items;
    }

    public static DoubleArrayNode valueOf(double[] items) {
        if (items == null) {
            return NULL;
        }
        if (items.length == 0) {
            return EMPTY;
        }
        return new DoubleArrayNode(items);
    }

    @Override
    public int size() {
        return items.length;
    }

    @Override
    public DataType dataType() {
        return DataType.DOUBLE;
    }

    @Override
    public ContextType contextType() {
        return null;
    }
}
