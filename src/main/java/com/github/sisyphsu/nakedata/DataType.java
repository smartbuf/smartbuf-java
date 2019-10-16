package com.github.sisyphsu.nakedata;

import lombok.Getter;

/**
 * @author sulin
 * @since 2019-05-08 20:25:32
 */
@Getter
public enum DataType {

    NULL,
    BOOL,
    FLOAT,
    DOUBLE,
    VARINT,
    STRING,
    SYMBOL,
    ARRAY,
    OBJECT,

    N_BOOL_ARRAY,
    N_BYTE_ARRAY,
    N_SHORT_ARRAY,
    N_INT_ARRAY,
    N_LONG_ARRAY,
    N_FLOAT_ARRAY,
    N_DOUBLE_ARRAY

}
