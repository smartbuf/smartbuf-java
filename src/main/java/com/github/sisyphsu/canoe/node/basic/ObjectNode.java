package com.github.sisyphsu.canoe.node.basic;

import com.github.sisyphsu.canoe.Const;
import com.github.sisyphsu.canoe.node.Node;

/**
 * ObjectNode represents JavaBean or Map.
 *
 * @author sulin
 * @since 2019-05-08 21:02:12
 */
public final class ObjectNode extends Node {

    public final static ObjectNode EMPTY = new ObjectNode(true, new String[0], new Object[0]);

    private final boolean  stable;
    private final String[] keys;
    private final Object[] nodes;

    public ObjectNode(boolean stable, String[] fields, Object[] nodes) {
        this.stable = stable;
        this.keys = fields;
        this.nodes = nodes;
    }

    public String[] keys() {
        return keys;
    }

    public Object[] values() {
        return nodes;
    }

    public boolean isStable() {
        return stable;
    }

    @Override
    public Object value() {
        return this;
    }

    @Override
    public byte type() {
        return Const.TYPE_OBJECT;
    }
}
