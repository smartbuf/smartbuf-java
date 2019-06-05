package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * string[] array, can't contains null
 *
 * @author sulin
 * @since 2019-06-04 16:52:57
 */
public class StringArrayNode extends ArrayNode {

    private String[] items;

    public StringArrayNode(String[] items) {
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
