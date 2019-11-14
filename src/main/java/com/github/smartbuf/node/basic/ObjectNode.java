package com.github.smartbuf.node.basic;

import com.github.smartbuf.Type;
import com.github.smartbuf.node.Node;
import com.github.smartbuf.node.NodeType;

/**
 * ObjectNode represents JavaBean or Map.
 *
 * @author sulin
 * @since 2019-05-08 21:02:12
 */
public final class ObjectNode extends Node {

    public final static ObjectNode EMPTY = new ObjectNode(true, new String[0], new Object[0]);

    /**
     * Stable object's keys must be ordered
     */
    private final boolean stable;

    private final String[] keys;
    private final Object[] fields;
    private final Type[]   types;

    public ObjectNode(boolean stable, String[] keys, Object[] values) {
        this.stable = stable;
        this.keys = keys;
        this.fields = values;
        this.types = null;
    }

    public ObjectNode(boolean stable, String[] keys, Object[] values, Type[] types) {
        this.stable = stable;
        this.keys = keys;
        this.fields = values;
        this.types = types;
    }

    public boolean isStable() {
        return stable;
    }

    public String[] keys() {
        return keys;
    }

    public Object[] values() {
        return fields;
    }

    public Type[] types() {
        return types;
    }

    @Override
    public Object value() {
        return this;
    }

    @Override
    public NodeType type() {
        return NodeType.OBJECT;
    }
}
