package com.github.sisyphsu.nakedata.node.container.slice;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.container.Slice;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * string[] slice, can't contains null
 *
 * @author sulin
 * @since 2019-06-04 16:52:57
 */
public class StringSlice extends Slice {

    private String[] items;

    public StringSlice(String[] items) {
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
        return DataType.STRING;
    }

    @Override
    public ContextType contextType() {
        return null;
    }

}
