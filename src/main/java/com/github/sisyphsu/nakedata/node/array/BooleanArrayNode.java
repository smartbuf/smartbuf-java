package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * boolean[] array
 *
 * @author sulin
 * @since 2019-06-04 19:53:42
 */
public class BooleanArrayNode extends ArrayNode {

    public static final BooleanArrayNode NULL = new BooleanArrayNode(null);
    public static final BooleanArrayNode EMPTY = new BooleanArrayNode(new boolean[0]);

    private boolean[] items;

    private BooleanArrayNode(boolean[] items) {
        this.items = items;
    }

    public static BooleanArrayNode valueOf(boolean[] items) {
        if (items == null) {
            return NULL;
        }
        if (items.length == 0) {
            return EMPTY;
        }
        return new BooleanArrayNode(items);
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
