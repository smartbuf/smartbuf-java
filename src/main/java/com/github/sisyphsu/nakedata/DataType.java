package com.github.sisyphsu.nakedata;

import com.fasterxml.jackson.databind.JsonNode;

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

    byte TRUE = 0x01;

    byte FALSE = 0x02;
    /**
     * Represent 4-byte's float
     */
    byte FLOAT = 0x03;
    /**
     * Represent 8-byte's double
     */
    byte DOUBLE = 0x04;
    /**
     * Represent [1,8] byte's varint, could be byte/short/int/long
     */
    byte NUMBER = 0x05;
    /**
     * Represent string
     */
    byte STRING = 0x06;
    /**
     * Represent byte[], used for convenience.
     */
    byte BINARY = 0x07;
    /**
     * Represent array, the DataType of items could be different.
     * If same, DataType could be UnionType.
     * If different, DataType should be seperated.
     */
    byte ARRAY = 0x08;
    /**
     * Represent object, Should be UnionType.
     */
    byte OBJECT = 0x09;

    /**
     * 获取指定node的标准类型代码
     *
     * @param node JSON节点
     * @return 标准类型代码
     */
    static byte parseType(JsonNode node) {
        if (node instanceof DataType) {
            return ((DataType) node).getTypeCode();
        } else {
            return DataType.NULL;
        }
    }

    byte getTypeCode();

}
