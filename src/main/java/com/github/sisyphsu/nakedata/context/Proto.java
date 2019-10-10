package com.github.sisyphsu.nakedata.context;

/**
 * @author sulin
 * @since 2019-10-02 16:42:24
 */
public interface Proto {

    byte ID_NULL  = 0x00;
    byte ID_FALSE = 0x01;
    byte ID_TRUE  = 0x02;

    byte VERSION     = (byte) 0b0100_0000;
    byte FLAG_STREAM = (byte) 0b0010_0000;
    byte FLAG_HEAD   = (byte) 0b0001_0000;

    byte CODE_FLOAT          = (byte) 1;
    byte CODE_DOUBLE         = (byte) 2;
    byte CODE_VARINT         = (byte) 3;
    byte CODE_STRING         = (byte) 4;
    byte CODE_NAMES          = (byte) 5;
    byte CODE_STRUCTS        = (byte) 6;
    byte CODE_NAME_ADDED     = (byte) 7;
    byte CODE_NAME_EXPIRED   = (byte) 8;
    byte CODE_STRUCT_ADDED   = (byte) 9;
    byte CODE_STRUCT_EXPIRED = (byte) 10;
    byte CODE_SYMBOL_ADDED   = (byte) 11;
    byte CODE_SYMBOL_EXPIRED = (byte) 12;

}
