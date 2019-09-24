package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.ArrayType;

import java.util.List;

/**
 * DoubleArrayNode represent an slice of Double object
 *
 * @author sulin
 * @since 2019-06-11 20:32:23
 */
public final class DoubleArrayNode extends ArrayNode {

    public DoubleArrayNode(List items) {
        super(items);
    }

    @Override
    public ArrayType elementType() {
        return ArrayType.DOUBLE;
    }

}
