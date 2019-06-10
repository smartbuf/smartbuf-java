package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.std.*;

/**
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

    public Node toNode(Character c) {
        return StringNode.valueOf(String.valueOf(c));
    }

    public Node toNode(String s) {
        return StringNode.valueOf(s);
    }

}
