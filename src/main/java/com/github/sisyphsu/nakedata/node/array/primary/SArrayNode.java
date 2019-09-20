package com.github.sisyphsu.nakedata.node.array.primary;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.DataType;

/**
 * short[] array
 *
 * @author sulin
 * @since 2019-06-05 15:54:42
 */
public class SArrayNode extends Node {

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

    public short[] getItems() {
        return items;
    }

    @Override
    public DataType dataType() {
        return DataType.SHORT;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
