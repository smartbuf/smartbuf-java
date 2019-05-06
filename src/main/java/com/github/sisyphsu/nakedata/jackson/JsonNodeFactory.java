package com.github.sisyphsu.nakedata.jackson;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 自定义JsonNodeFactory, 替换默认的ObjectNode实现
 *
 * @author sulin
 * @since 2019-05-03 18:43:58
 */
public class JsonNodeFactory extends com.fasterxml.jackson.databind.node.JsonNodeFactory {

    @Override
    public NullNode nullNode() {
        return super.nullNode();
    }

    @Override
    public BooleanNode booleanNode(boolean v) {
        return super.booleanNode(v);
    }

    @Override
    public NumericNode numberNode(byte v) {
        return super.numberNode(v);
    }

    @Override
    public ValueNode numberNode(Byte value) {
        return super.numberNode(value);
    }

    @Override
    public NumericNode numberNode(short v) {
        return super.numberNode(v);
    }

    @Override
    public ValueNode numberNode(Short value) {
        return super.numberNode(value);
    }

    @Override
    public NumericNode numberNode(int v) {
        return super.numberNode(v);
    }

    @Override
    public ValueNode numberNode(Integer value) {
        return super.numberNode(value);
    }

    @Override
    public NumericNode numberNode(long v) {
        return super.numberNode(v);
    }

    @Override
    public ValueNode numberNode(Long v) {
        return super.numberNode(v);
    }

    @Override
    public ValueNode numberNode(BigInteger v) {
        return super.numberNode(v);
    }

    @Override
    public NumericNode numberNode(float v) {
        return super.numberNode(v);
    }

    @Override
    public ValueNode numberNode(Float value) {
        return super.numberNode(value);
    }

    @Override
    public NumericNode numberNode(double v) {
        return super.numberNode(v);
    }

    @Override
    public ValueNode numberNode(Double value) {
        return super.numberNode(value);
    }

    @Override
    public ValueNode numberNode(BigDecimal v) {
        return super.numberNode(v);
    }

    @Override
    public TextNode textNode(String text) {
        return super.textNode(text);
    }

    @Override
    public BinaryNode binaryNode(byte[] data) {
        return super.binaryNode(data);
    }

    @Override
    public BinaryNode binaryNode(byte[] data, int offset, int length) {
        return super.binaryNode(data, offset, length);
    }

    @Override
    public ArrayNode arrayNode() {
        return super.arrayNode();
    }

    @Override
    public ArrayNode arrayNode(int capacity) {
        return super.arrayNode(capacity);
    }

    @Override
    public ObjectNode objectNode() {
        return new com.github.sisyphsu.nakedata.jackson.ObjectNode(this);
    }

    @Override
    public ValueNode pojoNode(Object pojo) {
        throw new IllegalArgumentException("Unsupported pojoNode: " + pojo);
    }

    @Override
    public ValueNode rawValueNode(RawValue value) {
        throw new IllegalArgumentException("Unsupported rawValueNode: " + value);
    }

}
