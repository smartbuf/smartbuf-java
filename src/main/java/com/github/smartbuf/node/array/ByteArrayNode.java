package com.github.smartbuf.node.array;

import com.github.smartbuf.node.Node;
import com.github.smartbuf.node.NodeType;

/**
 * ByteArrayNode represents byte[]
 *
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
    public NodeType type() {
        return NodeType.ARRAY_BYTE;
    }
}
