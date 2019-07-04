package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.type.DataType;

import java.util.List;

/**
 * FloatArrayNode represent an slice of Float object
 *
 * @author sulin
 * @since 2019-06-11 20:32:34
 */
public class FloatArrayNode extends ArrayNode {

    public FloatArrayNode(List items) {
        super(items);
    }

    @Override
    public DataType elementType() {
        return DataType.FLOAT;
    }

}