package com.github.sisyphsu.canoe.transport;

/**
 * Constants that shared between input and output
 *
 * @author sulin
 * @since 2019-10-02 16:42:24
 */
public interface Const {

    byte DATA_VARINT = 0;
    byte DATA_FLOAT  = 1;
    byte DATA_DOUBLE = 2;
    byte DATA_STRING = 3;
    byte DATA_SYMBOL = 4;
    byte DATA_OBJECT = 5;
    byte DATA_NARRAY = 6;
    byte DATA_ARRAY  = 7;

    byte NARRAY_BOOL   = 1 << 3 | DATA_NARRAY;
    byte NARRAY_BYTE   = 2 << 3 | DATA_NARRAY;
    byte NARRAY_SHORT  = 3 << 3 | DATA_NARRAY;
    byte NARRAY_INT    = 4 << 3 | DATA_NARRAY;
    byte NARRAY_LONG   = 5 << 3 | DATA_NARRAY;
    byte NARRAY_FLOAT  = 6 << 3 | DATA_NARRAY;
    byte NARRAY_DOUBLE = 7 << 3 | DATA_NARRAY;

    byte SLICE_NULL    = 0x00;
    byte SLICE_BOOL    = 0x01;
    byte SLICE_FLOAT   = 0x02;
    byte SLICE_DOUBLE  = 0x03;
    byte SLICE_BYTE    = 0x04;
    byte SLICE_SHORT   = 0x05;
    byte SLICE_INT     = 0x06;
    byte SLICE_LONG    = 0x07;
    byte SLICE_STRING  = 0x08;
    byte SLICE_SYMBOL  = 0x09;
    byte SLICE_OBJECT  = 0x0A;
    byte SLICE_UNKNOWN = 0x0B;

    @Deprecated
    byte SLICE_ARRAY = 0x0A;

    int  ID_PREFIX      = 8;
    byte ID_NULL        = 0x00;
    byte ID_FALSE       = 0x01;
    byte ID_TRUE        = 0x02;
    byte ID_ZERO_FLOAT  = 0x03;
    byte ID_ZERO_DOUBLE = 0x04;
    byte ID_ZERO_VARINT = 0x05;
    byte ID_ZERO_STRING = 0x06;
    byte ID_ZERO_ARRAY  = 0x07;

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

}
