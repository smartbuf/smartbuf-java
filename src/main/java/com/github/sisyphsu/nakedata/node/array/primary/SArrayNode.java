package com.github.sisyphsu.nakedata.node.array.primary;

import com.github.sisyphsu.nakedata.DataType;
import com.github.sisyphsu.nakedata.node.Node;

/**
 * short[] array
 *
 * @author sulin
 * @since 2019-06-05 15:54:42
 */
public final class SArrayNode extends Node {

    private short[] items;

    private SArrayNode(short[] items) {
        this.items = items;
    }

    public static SArrayNode valueOf(short[] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("data can't be null or empty");
        }
        return new SArrayNode(data);
    }

    @Override
    public short[] shortsValue() {
        return items;
    }

    @Override
    public DataType dataType() {
        return DataType.N_SHORT_ARRAY;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
