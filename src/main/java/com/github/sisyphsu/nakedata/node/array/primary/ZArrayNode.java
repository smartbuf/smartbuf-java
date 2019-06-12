package com.github.sisyphsu.nakedata.node.array.primary;

import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * boolean[] array
 *
 * @author sulin
 * @since 2019-06-04 19:53:42
 */
public class ZArrayNode extends ArrayNode {

    public static final ZArrayNode NULL = new ZArrayNode(null);
    public static final ZArrayNode EMPTY = new ZArrayNode(new boolean[0]);

    private boolean[] items;

    private ZArrayNode(boolean[] items) {
        this.items = items;
    }

    public static ZArrayNode valueOf(boolean[] items) {
        if (items == null) {
            return NULL;
        }
        if (items.length == 0) {
            return EMPTY;
        }
        return new ZArrayNode(items);
    }

    @Override
    public int size() {
        return items.length;
    }

    @Override
    public boolean tryAppend(Object o) {
        return false;
    }

    @Override
    public DataType elementDataType() {
        return DataType.BOOL;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

}
