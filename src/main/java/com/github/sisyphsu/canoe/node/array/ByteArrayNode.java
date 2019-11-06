package com.github.sisyphsu.canoe.node.array;

import com.github.sisyphsu.canoe.node.Node;

/**
 * @author sulin
 * @since 2019-11-03 14:46:42
 */
public final class ByteArrayNode extends Node {

    private final byte[] data;

    public ByteArrayNode(byte[] data) {
        this.data = data;
    }

    @Override
    public Object value() {
        return data;
    }

    @Override
    public Type type() {
        return Type.ARRAY_BYTE;
    }
}
