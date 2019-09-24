package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.ArrayType;

import java.util.List;

/**
 * IntegerArrayNode represent an array/slice of Integer object
 *
 * @author sulin
 * @since 2019-07-03 21:35:10
 */
public final class IntegerArrayNode extends ArrayNode {

    public IntegerArrayNode(List items) {
        super(items);
    }

    @Override
    public ArrayType elementType() {
        return ArrayType.INT;
    }

}
