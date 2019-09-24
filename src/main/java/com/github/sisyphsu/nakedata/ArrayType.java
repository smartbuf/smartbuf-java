package com.github.sisyphsu.nakedata;

/**
 * 数组类型枚举
 *
 * @author sulin
 * @since 2019-09-24 20:24:54
 */
public enum ArrayType {

    NULL(0x00),
    BOOL(0x01),
    FLOAT(0x02),
    DOUBLE(0x03),
    BYTE(0x04),
    SHORT(0x05),
    INT(0x06),
    LONG(0x07),
    STRING(0x08);

    private final byte code;

    ArrayType(int code) {
        this.code = (byte) code;
    }

    public byte getCode() {
        return code;
    }

}
