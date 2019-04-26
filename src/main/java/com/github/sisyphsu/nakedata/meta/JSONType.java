package com.github.sisyphsu.nakedata.meta;

/**
 * JSON's primary type
 *
 * @author sulin
 * @since 2019-04-25 15:22:52
 */
public interface JSONType {

    byte NULL = 0x00;
    byte TRUE = 0x01;
    byte FALSE = 0x02;
    byte NUMBER = 0x03;
    byte STRING = 0x04;
    byte ARRAY = 0x05;
    byte OBJECT = 0x06;
    byte FLOAT = 0x07;
    byte DOUBLE = 0x08;
    byte DECIMAL = 0x09;

}
