package com.github.sisyphsu.canoe.transport;

/**
 * Constants that shared between input and output
 *
 * @author sulin
 * @since 2019-10-02 16:42:24
 */
public interface Const {

    byte VER          = 0b0001_0000;
    byte VER_STREAM   = 0b0000_1000;
    byte VER_HAS_DATA = 0b0000_0100;
    byte VER_HAS_META = 0b0000_0010;
    byte VER_HAS_SEQ  = 0b0000_0001;

    byte META_NAME_TMP       = 1 << 1;
    byte META_NAME_ADDED     = 2 << 1;
    byte META_NAME_EXPIRED   = 3 << 1;
    byte META_STRUCT_TMP     = 4 << 1;
    byte META_STRUCT_ADDED   = 5 << 1;
    byte META_STRUCT_EXPIRED = 6 << 1;

    byte DATA_FLOAT          = 1 << 1;
    byte DATA_DOUBLE         = 2 << 1;
    byte DATA_VARINT         = 3 << 1;
    byte DATA_STRING         = 4 << 1;
    byte DATA_SYMBOL_ADDED   = 5 << 1;
    byte DATA_SYMBOL_EXPIRED = 6 << 1;

    byte DATA_ID_NULL       = 0x00;
    byte DATA_ID_FALSE      = 0x01;
    byte DATA_ID_TRUE       = 0x02;
    byte DATA_ID_ZERO_ARRAY = 0x03;

    byte TYPE_VARINT = 0;
    byte TYPE_FLOAT  = 1;
    byte TYPE_DOUBLE = 2;
    byte TYPE_STRING = 3;
    byte TYPE_SYMBOL = 4;
    byte TYPE_OBJECT = 5;
    byte TYPE_NARRAY = 6;
    byte TYPE_ARRAY  = 7;

    byte TYPE_NARRAY_BOOL   = 1 << 3 | TYPE_NARRAY;
    byte TYPE_NARRAY_BYTE   = 2 << 3 | TYPE_NARRAY;
    byte TYPE_NARRAY_SHORT  = 3 << 3 | TYPE_NARRAY;
    byte TYPE_NARRAY_INT    = 4 << 3 | TYPE_NARRAY;
    byte TYPE_NARRAY_LONG   = 5 << 3 | TYPE_NARRAY;
    byte TYPE_NARRAY_FLOAT  = 6 << 3 | TYPE_NARRAY;
    byte TYPE_NARRAY_DOUBLE = 7 << 3 | TYPE_NARRAY;

    byte TYPE_SLICE_NULL    = 0x00;
    byte TYPE_SLICE_BOOL    = 0x01;
    byte TYPE_SLICE_FLOAT   = 0x02;
    byte TYPE_SLICE_DOUBLE  = 0x03;
    byte TYPE_SLICE_BYTE    = 0x04;
    byte TYPE_SLICE_SHORT   = 0x05;
    byte TYPE_SLICE_INT     = 0x06;
    byte TYPE_SLICE_LONG    = 0x07;
    byte TYPE_SLICE_STRING  = 0x08;
    byte TYPE_SLICE_SYMBOL  = 0x09;
    byte TYPE_SLICE_OBJECT  = 0x0A;
    byte TYPE_SLICE_UNKNOWN = 0x0B;

}
