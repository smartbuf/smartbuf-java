package com.github.sisyphsu.nakedata.node.container.slice;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.container.Slice;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * float[] slice
 *
 * @author sulin
 * @since 2019-06-04 16:34:25
 */
public class FloatSlice extends Slice {

    private float[] items;

    public FloatSlice(float[] items) {
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
        return DataType.FLOAT;
    }

    @Override
    public ContextType contextType() {
        return null;
    }
}
