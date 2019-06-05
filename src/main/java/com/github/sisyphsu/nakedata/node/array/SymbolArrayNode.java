package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * symbol[] array, can't contains null.
 *
 * @author sulin
 * @since 2019-06-05 16:09:58
 */
public class SymbolArrayNode extends ArrayNode {

    private String[] items;

    public SymbolArrayNode(String[] items) {
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
