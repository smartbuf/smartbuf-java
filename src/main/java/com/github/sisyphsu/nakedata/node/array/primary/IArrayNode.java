package com.github.sisyphsu.nakedata.node.array.primary;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * int[] array
 *
 * @author sulin
 * @since 2019-06-05 15:54:27
 */
public class IArrayNode extends ArrayNode {

    public static final IArrayNode NULL = new IArrayNode(null);
    public static final IArrayNode EMPTY = new IArrayNode(new int[0]);

    private int[] items;

    private IArrayNode(int[] items) {
        this.items = items;
    }

    public static IArrayNode valueOf(int[] data) {
        if (data == null) {
            return NULL;
        }
        if (data.length == 0) {
            return EMPTY;
        }
        return new IArrayNode(data);
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
