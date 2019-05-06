package com.github.sisyphsu.nakedata.jackson.node;

import com.fasterxml.jackson.databind.node.BinaryNode;
import com.github.sisyphsu.nakedata.DataType;

/**
 * @author sulin
 * @since 2019-05-06 16:57:04
 */
public class NBinaryNode extends BinaryNode implements DataType {

    final static NBinaryNode EMPTY_BINARY_NODE = new NBinaryNode(new byte[0]);

    public NBinaryNode(byte[] data) {
        super(data);
    }

    public NBinaryNode(byte[] data, int offset, int length) {
        super(data, offset, length);
    }

    public static BinaryNode valueOf(byte[] data) {
        if (data == null) {
            return null;
        }
        if (data.length == 0) {
            return EMPTY_BINARY_NODE;
        }
        return new NBinaryNode(data);
    }

    public static BinaryNode valueOf(byte[] data, int offset, int length) {
        if (data == null) {
            return null;
        }
        if (length == 0) {
            return EMPTY_BINARY_NODE;
        }
        return new NBinaryNode(data, offset, length);
    }

    @Override
    public byte getTypeCode() {
        return BINARY;
    }

}
