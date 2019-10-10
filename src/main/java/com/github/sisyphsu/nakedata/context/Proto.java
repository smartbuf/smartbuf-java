package com.github.sisyphsu.nakedata.context;

/**
 * @author sulin
 * @since 2019-10-02 16:42:24
 */
public interface Proto {

    byte ID_NULL  = 0x00;
    byte ID_FALSE = 0x01;
    byte ID_TRUE  = 0x02;

    byte VERSION       = (byte) 0b0001_0000;
    byte FLAG_STREAM   = (byte) 0b0000_1000;
    byte FLAG_TMP_META = (byte) 0b0000_0100;
    byte FLAG_CXT_META = (byte) 0b0000_0010;

    byte TMP_FLOAT   = (byte) 1;
    byte TMP_DOUBLE  = (byte) 2;
    byte TMP_VARINT  = (byte) 3;
    byte TMP_STRING  = (byte) 4;
    byte TMP_NAMES   = (byte) 5;
    byte TMP_STRUCTS = (byte) 6;

    byte CXT_NAME_ADDED     = (byte) 1;
    byte CXT_STRUCT_ADDED   = (byte) 2;
    byte CXT_STRUCT_EXPIRED = (byte) 3;
    byte CXT_SYMBOL_ADDED   = (byte) 4;
    byte CXT_SYMBOL_EXPIRED = (byte) 5;

}
