package com.github.sisyphsu.canoe.node;

/**
 * Base class of Node, and provider some common features.
 *
 * @author sulin
 * @since 2019-05-08 20:33:51
 */
public abstract class Node {

    public abstract Object value();

    public abstract Type type();

    public enum Type {
        BOOLEAN,
        DOUBLE,
        FLOAT,
        VARINT,
        STRING,
        SYMBOL,
        OBJECT,
        ARRAY,
        ARRAY_BOOLEAN,
        ARRAY_BYTE,
        ARRAY_SHORT,
        ARRAY_INT,
        ARRAY_LONG,
        ARRAY_FLOAT,
        ARRAY_DOUBLE
    }

}
