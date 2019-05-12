package com.github.sisyphsu.nakedata.codec.concurrent;

import com.github.sisyphsu.nakedata.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.NodeMapper;
import com.github.sisyphsu.nakedata.node.std.DoubleNode;
import com.github.sisyphsu.nakedata.node.std.FloatNode;
import com.github.sisyphsu.nakedata.node.std.VarintNode;

import java.util.concurrent.atomic.DoubleAdder;

/**
 * DoubleAdder类型适配
 *
 * @author sulin
 * @since 2019-05-10 10:52:04
 */
public class DoubleAdderCodec extends Codec<DoubleAdder> {

    public DoubleAdderCodec(NodeMapper mapper) {
        super(DoubleAdder.class, mapper);
    }

    @Override
    public Node encode(DoubleAdder obj) {
        if (obj == null) {
            return DoubleNode.NULL;
        }
        return DoubleNode.valueOf(obj.doubleValue());
    }

    @Override
    public DoubleAdder decode(Node node) {
        if (node.isNull()) {
            return null;
        }
        DoubleAdder result = new DoubleAdder();
        if (node instanceof DoubleNode) {
            result.add(((DoubleNode) node).getValue());
        } else if (node instanceof FloatNode) {
            result.add(((FloatNode) node).getValue());
        } else if (node instanceof VarintNode) {
            result.add(((VarintNode) node).getValue());
        } else {
            throw new IllegalArgumentException("Can't convert data to DoubleAddr: " + node);
        }
        return result;
    }

}
