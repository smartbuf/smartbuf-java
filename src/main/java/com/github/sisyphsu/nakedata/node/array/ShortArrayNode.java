package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * short[] array
 *
 * @author sulin
 * @since 2019-06-05 15:54:42
 */
public class ShortArrayNode extends ArrayNode {

    public static final ShortArrayNode NULL = new ShortArrayNode(null);
    public static final ShortArrayNode EMPTY = new ShortArrayNode(new short[0]);

    private short[] items;

    private ShortArrayNode(short[] items) {
        this.items = items;
    }

    public static ShortArrayNode valueOf(short[] items) {
        if (items == null) {
            return NULL;
        }
        if (items.length == 0) {
            return EMPTY;
        }
        return new ShortArrayNode(items);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public DataType dataType() {
        return null;
    }

    @Override
    public ContextType contextType() {
        return null;
    }

}
