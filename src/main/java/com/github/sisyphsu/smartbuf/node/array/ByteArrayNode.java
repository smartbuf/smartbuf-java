package com.github.sisyphsu.smartbuf.node.array;

import com.github.sisyphsu.smartbuf.Const;
import com.github.sisyphsu.smartbuf.node.Node;

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
    public byte type() {
        return Const.TYPE_NARRAY_BYTE;
    }
}
