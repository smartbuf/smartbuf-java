package com.github.sisyphsu.nakedata;

/**
 * JSON's primary type
 *
 * @author sulin
 * @since 2019-04-25 15:22:52
 */
public interface DataType {

    /**
     * Represent null value
     */
    byte NULL = 0x00;
    /**
     * Represent boolean, could be true or false
     */
    byte BOOL = 0x01;
    /**
     * Represent 4-byte's float
     */
    byte FLOAT = 0x02;
    /**
     * Represent 8-byte's double
     */
    byte DOUBLE = 0x03;
    /**
     * Represent [1,8] byte's varint, could be byte/short/int/long
     */
    byte NUMBER = 0x04;
    /**
     * Represent string
     */
    byte STRING = 0x05;
    /**
     * Represent byte[], used for convenience.
     */
    byte BINARY = 0x06;
    /**
     * Represent array, the DataType of items could be different.
     * If same, DataType could be UnionType.
     * If different, DataType should be seperated.
     */
    byte ARRAY = 0x07;
    /**
     * Represent object, Should be UnionType.
     */
    byte OBJECT = 0x08;

}
