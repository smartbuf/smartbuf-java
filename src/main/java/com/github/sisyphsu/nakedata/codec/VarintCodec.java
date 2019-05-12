package com.github.sisyphsu.nakedata.codec;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.NodeMapper;
import com.github.sisyphsu.nakedata.node.std.StringNode;
import com.github.sisyphsu.nakedata.node.std.VarintNode;

/**
 * @author sulin
 * @since 2019-05-12 14:07:22
 */
public abstract class VarintCodec<T> extends Codec<T> {

    public VarintCodec(Class<T> type, NodeMapper mapper) {
        super(type, mapper);
    }

    @Override
    public final Node encode(T obj) {
        if (obj == null) {
            return VarintNode.NULL;
        }
        return VarintNode.valueOf(this.doEncode(obj));
    }

    @Override
    public final T decode(Node node) {
        if (node.isNull()) {
            return null;
        }
        Long l = null;
        if (node instanceof VarintNode) {
            l = ((VarintNode) node).getValue();
        } else if (node instanceof StringNode) {
            String str = ((StringNode) node).getValue();
            try {
                l = Long.parseLong(str);
            } catch (NumberFormatException ignored) {
            }
        }
        if (l == null) {
            throw new IllegalArgumentException("Cant convert to number: " + node);
        }
        return this.doDecode(l);
    }

    public abstract Long doEncode(T obj);

    public abstract T doDecode(Long l);

}
