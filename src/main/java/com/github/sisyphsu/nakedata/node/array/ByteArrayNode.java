package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.type.DataType;

import java.util.List;

/**
 * ByteArrayNode represent an slice of Byte object
 *
 * @author sulin
 * @since 2019-07-03 20:41:07
 */
public class ByteArrayNode extends ArrayNode {

    public ByteArrayNode(List items) {
        super(items);
    }

    @Override
    public DataType elementType() {
        return DataType.BYTE;
    }

}
