package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.type.DataType;

/**
 * @author sulin
 * @since 2019-05-08 21:01:31
 */
public class BinaryNode extends AbstractNode {

    public final static BinaryNode NULL = new BinaryNode(null);
    public final static BinaryNode EMPTY = new BinaryNode(new byte[0]);

    private final byte[] value;

    private BinaryNode(byte[] value) {
        this.value = value;
    }

    public static BinaryNode valueOf(byte[] bytes) {
        if (bytes == null)
            return NULL;
        if (bytes.length == 0)
            return EMPTY;
        return new BinaryNode(bytes);
    }

    @Override
    public DataType getType() {
        return DataType.BINARY;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

}
