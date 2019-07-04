package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.type.DataType;

import java.util.List;

/**
 * StringArrayNode represent an array/slice of String object
 *
 * @author sulin
 * @since 2019-06-04 16:52:57
 */
public class StringArrayNode extends ArrayNode {

    public StringArrayNode(List items) {
        super(items);
    }

    @Override
    public DataType elementType() {
        return DataType.STRING;
    }

}
