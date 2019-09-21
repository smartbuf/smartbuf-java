package com.github.sisyphsu.nakedata.node.array.primary;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.DataType;

/**
 * boolean[] array
 *
 * @author sulin
 * @since 2019-06-04 19:53:42
 */
public final class ZArrayNode extends Node {

    private boolean[] items;

    private ZArrayNode(boolean[] items) {
        this.items = items;
    }

    public static ZArrayNode valueOf(boolean[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data can't be null or empty");
        }
        return new ZArrayNode(data);
    }

    public boolean[] getItems() {
        return items;
    }

    @Override
    public DataType dataType() {
        return DataType.BOOL;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
