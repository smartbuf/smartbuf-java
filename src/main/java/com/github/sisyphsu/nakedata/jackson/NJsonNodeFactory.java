package com.github.sisyphsu.nakedata.jackson;

import com.fasterxml.jackson.databind.node.*;
import com.fasterxml.jackson.databind.util.RawValue;
import com.github.sisyphsu.nakedata.jackson.node.*;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 自定义JsonNodeFactory, 替换默认的ObjectNode实现
 *
 * @author sulin
 * @since 2019-05-03 18:43:58
 */
public class NJsonNodeFactory extends JsonNodeFactory {

    @Override
    public NullNode nullNode() {
        // FIXME: how to extend NullNode
        return super.nullNode();
    }

    @Override
    public BooleanNode booleanNode(boolean v) {
        return v ? NBooleanNode.TRUE : NBooleanNode.FALSE;
    }

    @Override
    public NumericNode numberNode(byte v) {
        return NVarintNode.valueOf(v);
    }

    @Override
    public ValueNode numberNode(Byte value) {
        return (value == null) ? nullNode() : numberNode(value.byteValue());
    }

    @Override
    public NumericNode numberNode(short v) {
        return NVarintNode.valueOf(v);
    }

    @Override
    public ValueNode numberNode(Short value) {
        return value == null ? nullNode() : numberNode(value.shortValue());
    }

    @Override
    public NumericNode numberNode(int v) {
        return NVarintNode.valueOf(v);
    }

    @Override
    public ValueNode numberNode(Integer value) {
        return value == null ? nullNode() : numberNode(value.intValue());
    }

    @Override
    public NumericNode numberNode(long v) {
        return NVarintNode.valueOf(v);
    }

    @Override
    public ValueNode numberNode(Long value) {
        return value == null ? nullNode() : numberNode(value.longValue());
    }

    @Override
    public ValueNode numberNode(BigInteger v) {
        return v == null ? nullNode() : textNode(v.toString());
    }

    @Override
    public NumericNode numberNode(float v) {
        return NFloatNode.valueOf(v);
    }

    @Override
    public ValueNode numberNode(Float value) {
        return value == null ? nullNode() : numberNode(value.floatValue());
    }

    @Override
    public NumericNode numberNode(double v) {
        return NDoubleNode.valueOf(v);
    }

    @Override
    public ValueNode numberNode(Double value) {
        return value == null ? nullNode() : numberNode(value.doubleValue());
    }

    @Override
    public ValueNode numberNode(BigDecimal v) {
        return v == null ? nullNode() : textNode(v.toString());
    }

    @Override
    public TextNode textNode(String text) {
        return NTextNode.valueOf(text);
    }

    @Override
    public BinaryNode binaryNode(byte[] data) {
        return NBinaryNode.valueOf(data);
    }

    @Override
    public BinaryNode binaryNode(byte[] data, int offset, int length) {
        return NBinaryNode.valueOf(data, offset, length);
    }

    @Override
    public ArrayNode arrayNode() {
        return new NArrayNode(this);
    }

    @Override
    public ArrayNode arrayNode(int capacity) {
        return new NArrayNode(this, capacity);
    }

    @Override
    public ObjectNode objectNode() {
        return new NObjectNode(this);
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
