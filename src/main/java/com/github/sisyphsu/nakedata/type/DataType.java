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
    BYTE(0x04),
    SHORT(0x05),
    INT(0x06),
    LONG(0x07),
    VARINT(0x08),
    STRING(0x09),
    SYMBOL(0x10),
    ARRAY(0x11),
    OBJECT(0x12);

    private final byte code;

    DataType(int code) {
        this.code = (byte) code;
    }

}
