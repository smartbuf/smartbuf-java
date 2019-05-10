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
    ENUM(0x02),
    FLOAT(0x03),
    DOUBLE(0x04),
    VARINT(0x05),
    STRING(0x06),
    BINARY(0x07),
    ARRAY(0x08),
    OBJECT(0x09),
    BIGINTEGER(0x10),
    BIGDECIMAL(0x11);

    private final byte code;

    DataType(int code) {
        this.code = (byte) code;
    }

}
