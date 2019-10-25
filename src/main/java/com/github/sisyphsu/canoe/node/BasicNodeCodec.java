package com.github.sisyphsu.canoe.node;

import com.github.sisyphsu.canoe.convertor.Codec;
import com.github.sisyphsu.canoe.convertor.Converter;
import com.github.sisyphsu.canoe.node.std.*;

/**
 * codec between standard-data and node, except array/object/collection/etc.
 *
 * @author sulin
 * @since 2019-06-10 21:02:55
 */
public final class BasicNodeCodec extends Codec {

    @Converter
    public Node toNode(Boolean b) {
        return BooleanNode.valueOf(b);
    }

    @Converter
    public Node toNode(Byte b) {
        return VarintNode.valueOf(b);
    }

    @Converter
    public Node toNode(Short s) {
        return VarintNode.valueOf(s);
    }

    @Converter
    public Node toNode(Integer i) {
        return VarintNode.valueOf(i);
    }

    @Converter
    public Node toNode(Long l) {
        return VarintNode.valueOf(l);
    }

    @Converter
    public Node toNode(Float f) {
        return FloatNode.valueOf(f);
    }

    @Converter
    public Node toNode(Double d) {
        return DoubleNode.valueOf(d);
    }

    @Converter
    public Node toNode(String s) {
        return StringNode.valueOf(s);
    }

    @Converter
    public Boolean toBoolean(BooleanNode node) {
        return node.booleanValue();
    }

    @Converter
    public Byte toByte(VarintNode node) {
        return (byte) node.longValue();
    }

    @Converter
    public Short toShort(VarintNode node) {
        return (short) node.longValue();
    }

    @Converter
    public Integer toInt(VarintNode node) {
        return (int) node.longValue();
    }

    @Converter
    public Long toLong(VarintNode node) {
        return node.longValue();
    }

    @Converter
    public Float toFloat(FloatNode node) {
        return node.floatValue();
    }

    @Converter
    public Double toDouble(DoubleNode node) {
        return node.doubleValue();
    }

    @Converter
    public String toString(StringNode node) {
        return node.stringValue();
    }

    @Converter
    public String toString(SymbolNode node) {
        return node.stringValue();
    }

    /**
     * enum could be encoded to symble directly, but don't need decoded directly.
     *
     * @param e enum
     * @return SymbolNode
     */
    @Converter
    public Node toNode(Enum e) {
        return SymbolNode.valueOf(e);
    }

}
