package com.github.sisyphsu.datatube.node;

/**
 * Base class of Node, and provider some common features.
 *
 * @author sulin
 * @since 2019-05-08 20:33:51
 */
public abstract class Node {

    /**
     * Get Node's dataType
     *
     * @return DataType
     */
    public abstract NodeType type();

    public boolean booleanValue() {
        throw new UnsupportedOperationException();
    }

    public float floatValue() {
        throw new UnsupportedOperationException();
    }

    public double doubleValue() {
        throw new UnsupportedOperationException();
    }

    public long longValue() {
        throw new UnsupportedOperationException();
    }

    public String stringValue() {
        throw new UnsupportedOperationException();
    }

}
