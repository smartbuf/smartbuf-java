package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.DataType;

import java.util.List;

/**
 * IntegerArrayNode represent an array/slice of Integer object
 *
 * @author sulin
 * @since 2019-07-03 21:35:10
 */
public class IntegerArrayNode extends ArrayNode {

    public IntegerArrayNode(List items) {
        super(items);
    }

    @Override
    public DataType elementType() {
        return DataType.INT;
    }

}
