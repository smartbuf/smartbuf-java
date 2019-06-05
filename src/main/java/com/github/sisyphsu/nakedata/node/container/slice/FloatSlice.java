package com.github.sisyphsu.nakedata.node.container.slice;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * @author sulin
 * @since 2019-06-04 16:34:25
 */
public class FloatSlice extends Node {

    private float[] data;

    @Override
    public DataType getDataType() {
        return DataType.ARRAY;
    }

    @Override
    public boolean isNull() {
        return false;
    }
}
