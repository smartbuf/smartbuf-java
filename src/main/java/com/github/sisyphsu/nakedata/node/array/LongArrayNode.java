package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * long[] array
 *
 * @author sulin
 * @since 2019-06-05 15:54:35
 */
public class LongArrayNode extends ArrayNode {

    public static final LongArrayNode NULL = new LongArrayNode(null);
    public static final LongArrayNode EMPTY = new LongArrayNode(new long[0]);

    private long[] items;

    private LongArrayNode(long[] items) {
        this.items = items;
    }

    public static LongArrayNode valueOf(long[] data) {
        if (data == null) {
            return NULL;
        }
        if (data.length == 0) {
            return EMPTY;
        }
        return new LongArrayNode(data);
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
