package com.github.sisyphsu.nakedata.node.container.slice;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.container.Slice;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * int[] slice
 *
 * @author sulin
 * @since 2019-06-05 15:54:27
 */
public class IntSlice extends Slice {

    private int[] items;

    public IntSlice(int[] items) {
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
