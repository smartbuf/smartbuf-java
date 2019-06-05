package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * int[] array
 *
 * @author sulin
 * @since 2019-06-05 15:54:27
 */
public class IntArrayNode extends ArrayNode {

    public static final IntArrayNode NULL = new IntArrayNode(null);
    public static final IntArrayNode EMPTY = new IntArrayNode(new int[0]);

    private int[] items;

    private IntArrayNode(int[] items) {
        this.items = items;
    }

    public static IntArrayNode valueOf(int[] data) {
        if (data == null) {
            return NULL;
        }
        if (data.length == 0) {
            return EMPTY;
        }
        return new IntArrayNode(data);
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
