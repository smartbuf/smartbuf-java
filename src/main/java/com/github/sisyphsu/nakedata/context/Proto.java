package com.github.sisyphsu.nakedata.context;

/**
 * @author sulin
 * @since 2019-10-02 16:42:24
 */
public interface Proto {

    byte ID_NULL   = 0x00;
    byte ID_FALSE  = 0x01;
    byte ID_TRUE   = 0x02;
    int  ID_PREFIX = 4;

    byte VER        = 0b0001_0000;
    byte F_STREAM   = 0b0000_1000;
    byte F_TMP_META = 0b0000_0100;
    byte F_CXT_META = 0b0000_0010;

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

    byte BODY_FLAG_DATA   = 0b0000_0011;
    byte BODY_FLAG_ARRAY  = 0b0000_0010;
    byte BODY_FLAG_STRUCT = 0b0000_0001;

}
