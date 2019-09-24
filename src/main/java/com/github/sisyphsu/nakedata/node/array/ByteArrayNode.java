package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.ArrayType;

import java.util.List;

/**
 * ByteArrayNode represent an slice of Byte object
 *
 * @author sulin
 * @since 2019-07-03 20:41:07
 */
public final class ByteArrayNode extends ArrayNode {

    public ByteArrayNode(List items) {
        super(items);
    }

    @Override
    public ArrayType elementType() {
        return ArrayType.BYTE;
    }

}
