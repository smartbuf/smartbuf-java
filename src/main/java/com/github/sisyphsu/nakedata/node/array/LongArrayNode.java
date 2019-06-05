package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * long[] array
 *
 * @author sulin
 * @since 2019-06-05 15:54:35
 */
public class LongArrayNode extends ArrayNode {

    private long[] items;

    public LongArrayNode(long[] items) {
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
        return null;
    }

    @Override
    public ContextType contextType() {
        return null;
    }
}
