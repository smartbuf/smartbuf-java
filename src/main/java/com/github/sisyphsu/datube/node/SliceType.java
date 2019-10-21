package com.github.sisyphsu.datube.node;

/**
 * SliceType represent the element type of slice, which composes ArrayNode,
 * It support native array and part of Object[].
 *
 * @author sulin
 * @since 2019-09-24 20:24:54
 */
public enum SliceType {

    NULL,
    BOOL,
    BYTE,
    SHORT,
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    STRING,
    SYMBOL,
    ARRAY,
    OBJECT,

    BOOL_NATIVE,
    BYTE_NATIVE,
    SHORT_NATIVE,
    INT_NATIVE,
    LONG_NATIVE,
    FLOAT_NATIVE,
    DOUBLE_NATIVE,

}
