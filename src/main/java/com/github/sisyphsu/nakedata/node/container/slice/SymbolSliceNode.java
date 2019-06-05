package com.github.sisyphsu.nakedata.node.container.slice;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.container.SliceNode;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * symbol[] slice, can't contains null.
 *
 * @author sulin
 * @since 2019-06-05 16:09:58
 */
public class SymbolSliceNode extends SliceNode {

    private String[] items;

    public SymbolSliceNode(String[] items) {
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
        return DataType.SYMBOL;
    }

    @Override
    public ContextType contextType() {
        return null;
    }

}
