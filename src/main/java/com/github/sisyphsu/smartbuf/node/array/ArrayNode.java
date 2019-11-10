package com.github.sisyphsu.smartbuf.node.array;

import com.github.sisyphsu.smartbuf.node.Node;
import com.github.sisyphsu.smartbuf.node.NodeType;

import java.util.Collection;

/**
 * ArrayNode represents unknown data's array
 *
 * @author sulin
 * @since 2019-11-03 14:54:55
 */
public final class ArrayNode extends Node {

    private final Collection<?> data;

    public ArrayNode(Collection<?> data) {
        this.data = data;
    }

    @Override
    public Object value() {
        return data;
    }

    @Override
    public NodeType type() {
        return NodeType.ARRAY;
    }
}
