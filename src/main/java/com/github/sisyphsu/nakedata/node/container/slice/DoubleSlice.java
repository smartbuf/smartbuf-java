package com.github.sisyphsu.nakedata.node.container.slice;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * @author sulin
 * @since 2019-06-04 16:51:04
 */
public class DoubleSlice extends Node {

    @Override
    public DataType getDataType() {
        return DataType.ARRAY;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
