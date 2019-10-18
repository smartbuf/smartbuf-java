package com.github.sisyphsu.nakedata;

/**
 * 数组片段类型枚举：
 * 1. 原生数组，如boolean[], byte[], short[], int[], long[], float[], double[]
 * 2. 对象数组，如List<Byte>
 * 3. Node数组，如List<BooleanNode>
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
