package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.DataType;

import java.util.List;

/**
 * LongArrayNode represent an array/slice of Long object
 *
 * @author sulin
 * @since 2019-06-11 20:32:52
 */
public final class LongArrayNode extends ArrayNode {

    public LongArrayNode(List items) {
        super(items);
    }

    @Override
    public DataType elementType() {
        return DataType.LONG;
    }

}
