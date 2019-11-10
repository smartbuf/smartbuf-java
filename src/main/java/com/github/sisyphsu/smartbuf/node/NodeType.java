package com.github.sisyphsu.smartbuf.node;

/**
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
