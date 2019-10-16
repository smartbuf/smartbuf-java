package com.github.sisyphsu.nakedata.proto;

import com.github.sisyphsu.nakedata.ArrayType;

/**
 * @author sulin
 * @since 2019-10-02 16:42:24
 */
public interface Const {

    byte ID_NULL   = 0x00;
    byte ID_FALSE  = 0x01;
    byte ID_TRUE   = 0x02;
    int  ID_PREFIX = 4;

    byte VER          = 0b0001_0000;
    byte VER_STREAM   = 0b0000_1000;
    byte VER_TMP_META = 0b0000_0100;
    byte VER_CXT_META = 0b0000_0010;

    byte TMP_FLOAT   = 1;
    byte TMP_DOUBLE  = 2;
    byte TMP_VARINT  = 3;
    byte TMP_STRING  = 4;
    byte TMP_NAMES   = 5;
    byte TMP_STRUCTS = 6;

    byte CXT_NAME_ADDED     = 1;
    byte CXT_NAME_EXPIRED   = 2;
    byte CXT_STRUCT_ADDED   = 3;
    byte CXT_STRUCT_EXPIRED = 4;
    byte CXT_SYMBOL_ADDED   = 5;
    byte CXT_SYMBOL_EXPIRED = 6;

    byte FLAG_DATA   = 0b0000_0011;
    byte FLAG_ARRAY  = 0b0000_0010;
    byte FLAG_STRUCT = 0b0000_0001;

    byte SLICE_NULL   = 0x00;
    byte SLICE_BOOL   = 0x01;
    byte SLICE_FLOAT  = 0x02;
    byte SLICE_DOUBLE = 0x03;
    byte SLICE_BYTE   = 0x04;
    byte SLICE_SHORT  = 0x05;
    byte SLICE_INT    = 0x06;
    byte SLICE_LONG   = 0x07;
    byte SLICE_STRING = 0x08;
    byte SLICE_SYMBOL = 0x09;
    byte SLICE_ARRAY  = 0x0A;
    byte SLICE_OBJECT = 0x0B;

    static byte toSliceType(ArrayType type) {
        switch (type) {
            case NULL:
                return SLICE_NULL;
            case BOOL:
                return SLICE_BOOL;
            case FLOAT:
                return SLICE_FLOAT;
            case DOUBLE:
                return SLICE_DOUBLE;
            case BYTE:
                return SLICE_BYTE;
            case SHORT:
                return SLICE_SHORT;
            case INT:
                return SLICE_INT;
            case LONG:
                return SLICE_LONG;
            case STRING:
                return SLICE_STRING;
            case SYMBOL:
                return SLICE_SYMBOL;
            case ARRAY:
                return SLICE_ARRAY;
            case OBJECT:
                return SLICE_OBJECT;
            default:
                throw new IllegalArgumentException("unknown ArrayType: " + type);
        }
    }

}
