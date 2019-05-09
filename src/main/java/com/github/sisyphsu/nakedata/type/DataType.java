package com.github.sisyphsu.nakedata.type;

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
    BINARY(0x06),
    ARRAY(0x07),
    OBJECT(0x08),
    BIGINTEGER(0x09),
    BIGDECIMAL(0x10);

    private final byte code;

    DataType(int code) {
        this.code = (byte) code;
    }

}
