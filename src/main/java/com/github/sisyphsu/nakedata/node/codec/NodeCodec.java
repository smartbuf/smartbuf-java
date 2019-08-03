package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.std.*;

/**
 * codec between standard-data and node, except array/object/collection/etc.
 *
 * @author sulin
 * @since 2019-06-10 21:02:55
 */
public class NodeCodec extends Codec {

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

    /**
     * enum could be encoded to symble directly, but don't need decoded directly.
     *
     * @param e enum
     * @return SymbolNode
     */
    @Converter
    public Node toNode(Enum e) {
        String name = e.name();
        return SymbolNode.valueOf(name);
    }

    @Converter
    public Boolean toBoolean(BooleanNode node) {
        return node.value();
    }

    @Converter
    public Byte toByte(VarintNode node) {
        if (node.isNull()) {
            return null;
        }
        return (byte) node.getValue();
    }

    @Converter
    public Short toShort(VarintNode node) {
        if (node.isNull()) {
            return null;
        }
        return (short) node.getValue();
    }

    @Converter
    public Integer toInt(VarintNode node) {
        if (node.isNull()) {
            return null;
        }
        return (int) node.getValue();
    }

    @Converter
    public Long toLong(VarintNode node) {
        if (node.isNull()) {
            return null;
        }
        return node.getValue();
    }

    @Converter
    public Float toFloat(FloatNode node) {
        if (node.isNull()) {
            return null;
        }
        return node.getValue();
    }

    @Converter
    public Double toDouble(DoubleNode node) {
        if (node.isNull()) {
            return null;
        }
        return node.getValue();
    }

    @Converter
    public String toString(StringNode node) {
        if (node.isNull()) {
            return null;
        }
        return node.getValue();
    }

    @Converter
    public String toString(SymbolNode node) {
        return node.getData();
    }

}
