package com.github.sisyphsu.nakedata.node.container.slice;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.container.Slice;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * boolean[] slice
 *
 * @author sulin
 * @since 2019-06-04 19:53:42
 */
public class BooleanSlice extends Slice {

    private boolean[] items;

    public BooleanSlice(boolean[] items) {
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
        return DataType.BOOL;
    }

    @Override
    public ContextType contextType() {
        return null;
    }

}
