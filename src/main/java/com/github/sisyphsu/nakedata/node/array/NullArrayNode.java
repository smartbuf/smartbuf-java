package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.DataType;

import java.util.List;

/**
 * FloatArrayNode represents an null array or slice, only record count.
 *
 * @author sulin
 * @since 2019-06-05 16:07:43
 */
public class NullArrayNode extends ArrayNode {

    public NullArrayNode(List items) {
        super(items);
    }

    @Override
    public DataType elementType() {
        return DataType.NULL;
    }

}
