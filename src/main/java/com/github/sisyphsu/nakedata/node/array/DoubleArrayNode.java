package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.DataType;

import java.util.List;

/**
 * DoubleArrayNode represent an slice of Double object
 *
 * @author sulin
 * @since 2019-06-11 20:32:23
 */
public class DoubleArrayNode extends ArrayNode {

    public DoubleArrayNode(List items) {
        super(items);
    }

    @Override
    public DataType elementType() {
        return DataType.DOUBLE;
    }

}
