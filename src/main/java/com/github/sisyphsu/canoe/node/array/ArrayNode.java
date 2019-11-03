package com.github.sisyphsu.canoe.node.array;

import com.github.sisyphsu.canoe.node.Node;

import java.util.List;

/**
 * @author sulin
 * @since 2019-11-03 14:54:55
 */
public final class ArrayNode extends Node {

    private final List<?> data;

    public ArrayNode(List<?> data) {
        this.data = data;
    }

    @Override
    public Object value() {
        return data;
    }

    @Override
    public List<?> arrayValue() {
        return data;
    }
}
