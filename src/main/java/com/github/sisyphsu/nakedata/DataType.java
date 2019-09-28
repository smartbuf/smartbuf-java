package com.github.sisyphsu.nakedata;

import lombok.Getter;

/**
 * @author sulin
 * @since 2019-05-08 20:25:32
 */
@Getter
public enum DataType {

    NULL(0x00),
    BOOL(0x01),
    FLOAT(0x02),
    DOUBLE(0x03),
    VARINT(0x04),
    STRING(0x05),
    SYMBOL(0x06),
    ARRAY(0x07),
    OBJECT(0x08),

    N_BOOL_ARRAY(0x10),
    N_BYTE_ARRAY(0x11),
    N_SHORT_ARRAY(0x12),
    N_INT_ARRAY(0x13),
    N_LONG_ARRAY(0x14),
    N_FLOAT_ARRAY(0x15),
    N_DOUBLE_ARRAY(0x16);

    private final byte code;

    DataType(int code) {
        this.code = (byte) code;
    }

}
