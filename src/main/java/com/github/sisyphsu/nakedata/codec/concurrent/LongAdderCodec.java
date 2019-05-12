package com.github.sisyphsu.nakedata.codec.concurrent;

import com.github.sisyphsu.nakedata.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.NodeMapper;
import com.github.sisyphsu.nakedata.node.std.VarintNode;

import java.util.concurrent.atomic.LongAdder;

/**
 * LongAdder类型适配
 *
 * @author sulin
 * @since 2019-05-10 10:51:53
 */
public class LongAdderCodec extends Codec<LongAdder> {

    public LongAdderCodec(NodeMapper mapper) {
        super(LongAdder.class, mapper);
    }

    @Override
    public Node encode(LongAdder obj) {
        if (obj == null) {
            return VarintNode.NULL;
        }
        return VarintNode.valueOf(obj.longValue());
    }

    @Override
    public LongAdder decode(Node node) {
        if (node == null) {
            return null;
        }
        LongAdder result = new LongAdder();
        if (node instanceof VarintNode) {
            result.add(((VarintNode) node).getValue());
        } else {
            throw new IllegalArgumentException("Can't convert data to LongAdder: " + node);
        }
        return result;
    }

}
