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

    public static final StringArrayNode NULL = new StringArrayNode(null);
    public static final StringArrayNode EMPTY = new StringArrayNode(new String[0]);

    private String[] items;

    private StringArrayNode(String[] items) {
        this.items = items;
    }

    public static StringArrayNode valueOf(String[] items) {
        if (items == null) {
            return NULL;
        }
        if (items.length == 0) {
            return EMPTY;
        }
        return new StringArrayNode(items);
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
