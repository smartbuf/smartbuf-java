package com.github.sisyphsu.nakedata;

import lombok.Getter;

/**
 * @author sulin
 * @since 2019-05-08 20:25:32
 */
@Getter
public enum NodeType {

    UNKNOWN,

    BOOL,
    FLOAT,
    DOUBLE,
    VARINT,
    STRING,
    SYMBOL,
    OBJECT,
    ARRAY

}
