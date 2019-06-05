package com.github.sisyphsu.nakedata.node.container.slice;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * @author sulin
 * @since 2019-06-04 16:52:57
 */
public class StringSlice extends Node {

    @Override
    public DataType getDataType() {
        return DataType.ARRAY;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
