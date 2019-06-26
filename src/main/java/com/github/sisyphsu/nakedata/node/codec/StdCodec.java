package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.std.*;

/**
 * codec between standard-data and node, except array/object/collection/etc.
 *
 * @author sulin
 * @since 2019-06-10 21:02:55
 */
public class StdCodec extends Codec {

    public Node toNode(Boolean b) {
        return BooleanNode.valueOf(b);
    }

    public Node toNode(Byte b) {
        return VarintNode.valueOf(b);
    }

    public Node toNode(Short s) {
        return VarintNode.valueOf(s);
    }

    public Node toNode(Integer i) {
        return VarintNode.valueOf(i);
    }

    public Node toNode(Long l) {
        return VarintNode.valueOf(l);
    }

    public Node toNode(Float f) {
        return FloatNode.valueOf(f);
    }

    public Node toNode(Double d) {
        return DoubleNode.valueOf(d);
    }

    public Node toNode(String s) {
        return StringNode.valueOf(s);
    }

    /**
     * enum could be encoded to symble directly, but don't need decoded directly.
     *
     * @param e enum
     * @return SymbolNode
     */
    public Node toNode(Enum e) {
        String name = e.name();
        return SymbolNode.valueOf(name);
    }

    public Boolean toBoolean(BooleanNode node) {
        return node.value();
    }

    public Byte toByte(VarintNode node) {
        if (node.isNull()) {
            return null;
        }
        return (byte) node.getValue();
    }

    public Short toShort(VarintNode node) {
        if (node.isNull()) {
            return null;
        }
        return (short) node.getValue();
    }

    public Integer toInt(VarintNode node) {
        if (node.isNull()) {
            return null;
        }
        return (int) node.getValue();
    }

    public Long toLong(VarintNode node) {
        if (node.isNull()) {
            return null;
        }
        return node.getValue();
    }

    public Float toFloat(FloatNode node) {
        if (node.isNull()) {
            return null;
        }
        return node.getValue();
    }

    public Double toDouble(DoubleNode node) {
        if (node.isNull()) {
            return null;
        }
        return node.getValue();
    }

    public String toString(StringNode node) {
        if (node.isNull()) {
            return null;
        }
        return node.getValue();
    }

    public String toString(SymbolNode node) {
        return node.getData();
    }

}
