package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.type.DataType;

import java.util.List;

/**
 * ShortArrayNode represent an array/slice of Short object
 *
 * @author sulin
 * @since 2019-07-03 21:33:48
 */
public class ShortArrayNode extends ArrayNode {

    public ShortArrayNode(List items) {
        super(items);
    }

    @Override
    public DataType elementType() {
        return DataType.SHORT;
    }

}
