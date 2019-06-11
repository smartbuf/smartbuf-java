package com.github.sisyphsu.nakedata.node.array.primary;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;

/**
 * double[] array
 *
 * @author sulin
 * @since 2019-06-04 16:51:04
 */
public class DArrayNode extends ArrayNode {

    public static final DArrayNode NULL = new DArrayNode(null);
    public static final DArrayNode EMPTY = new DArrayNode(new double[0]);

    private double[] items;

    private DArrayNode(double[] items) {
        this.items = items;
    }

    public static DArrayNode valueOf(double[] items) {
        if (items == null) {
            return NULL;
        }
        if (items.length == 0) {
            return EMPTY;
        }
        return new DArrayNode(items);
    }

    @Override
    public int size() {
        return items.length;
    }

    @Override
    public ContextType contextType() {
        return null;
    }
}
