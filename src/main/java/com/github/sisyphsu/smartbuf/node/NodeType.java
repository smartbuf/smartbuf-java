package com.github.sisyphsu.smartbuf.node;

/**
 * NodeType represents datatype of {@link Node},
 * it could help use switch to optimize a little performance.
 *
 * @author sulin
 * @since 2019-11-10 12:12:51
 */
public enum NodeType {
    UNKNOWN,

    BOOLEAN,
    VARINT,
    FLOAT,
    DOUBLE,
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
