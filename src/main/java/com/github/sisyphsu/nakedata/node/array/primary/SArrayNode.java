package com.github.sisyphsu.nakedata.node.array.primary;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * short[] array
 *
 * @author sulin
 * @since 2019-06-05 15:54:42
 */
public class SArrayNode extends ArrayNode {

    public static final SArrayNode NULL = new SArrayNode(null);
    public static final SArrayNode EMPTY = new SArrayNode(new short[0]);

    private short[] items;

    private SArrayNode(short[] items) {
        this.items = items;
    }

    public static SArrayNode valueOf(short[] items) {
        if (items == null) {
            return NULL;
        }
        if (items.length == 0) {
            return EMPTY;
        }
        return new SArrayNode(items);
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
