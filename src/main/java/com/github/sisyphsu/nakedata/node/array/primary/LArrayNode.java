package com.github.sisyphsu.nakedata.node.array.primary;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * long[] array
 *
 * @author sulin
 * @since 2019-06-05 15:54:35
 */
public class LArrayNode extends ArrayNode {

    public static final LArrayNode NULL = new LArrayNode(null);
    public static final LArrayNode EMPTY = new LArrayNode(new long[0]);

    private long[] items;

    private LArrayNode(long[] items) {
        this.items = items;
    }

    public static LArrayNode valueOf(long[] data) {
        if (data == null) {
            return NULL;
        }
        if (data.length == 0) {
            return EMPTY;
        }
        return new LArrayNode(data);
    }

    @Override
    public int size() {
        return items.length;
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
