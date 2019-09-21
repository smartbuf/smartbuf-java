package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.DataType;

import java.util.List;

/**
 * BooleanArrayNode represent an slice of Boolean object
 *
 * @author sulin
 * @since 2019-06-11 20:32:06
 */
public final class BooleanArrayNode extends ArrayNode {

    public BooleanArrayNode(List items) {
        super(items);
    }

    @Override
    public DataType elementType() {
        return DataType.BOOL;
    }

}
