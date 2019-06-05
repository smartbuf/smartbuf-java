package com.github.sisyphsu.nakedata.node.container.slice;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.container.SliceNode;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * float[] slice
 *
 * @author sulin
 * @since 2019-06-04 16:34:25
 */
public class FloatSliceNode extends SliceNode {

    private float[] items;

    public FloatSliceNode(float[] items) {
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
