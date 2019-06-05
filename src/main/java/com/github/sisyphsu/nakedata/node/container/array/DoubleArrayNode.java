package com.github.sisyphsu.nakedata.node.container.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.container.ArrayNode;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * double[] array
 *
 * @author sulin
 * @since 2019-06-04 16:51:04
 */
public class DoubleArrayNode extends ArrayNode {

    private double[] items;

    public DoubleArrayNode(double[] items) {
        if (items == null) {
            throw new IllegalArgumentException("items can't be null");
        }
        this.items = items;
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
