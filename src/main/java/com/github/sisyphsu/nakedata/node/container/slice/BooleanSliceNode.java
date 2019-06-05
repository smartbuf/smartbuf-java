package com.github.sisyphsu.nakedata.node.container.slice;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.container.SliceNode;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * boolean[] slice
 *
 * @author sulin
 * @since 2019-06-04 19:53:42
 */
public class BooleanSliceNode extends SliceNode {

    private boolean[] items;

    public BooleanSliceNode(boolean[] items) {
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
