package com.github.sisyphsu.nakedata.node;

/**
 * @author sulin
 * @since 2019-05-08 21:01:31
 */
public class BinaryNode extends AbstractNode {

    public final static BinaryNode NULL = new BinaryNode();
    public final static BinaryNode EMPTY = new BinaryNode(new byte[0]);

    private final boolean nil;
    private final byte[] value;

    private BinaryNode() {
        this.nil = true;
        this.value = null;
    }

    private BinaryNode(byte[] value) {
        this.nil = false;
        this.value = value;
    }

    public static BinaryNode valueOf(byte[] bytes) {
        if (bytes == null)
            return NULL;
        if (bytes.length == 0)
            return EMPTY;
        return new BinaryNode(bytes);
    }

}
