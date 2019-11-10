package com.github.sisyphsu.smartbuf.node.array;

import com.github.sisyphsu.smartbuf.Const;
import com.github.sisyphsu.smartbuf.node.Node;

/**
 * @author sulin
 * @since 2019-11-03 14:47:16
 */
public final class FloatArrayNode extends Node {

    private final float[] data;

    public FloatArrayNode(float[] data) {
        this.data = data;
    }

    @Override
    public Object value() {
        return data;
    }

    @Override
    public byte type() {
        return Const.TYPE_NARRAY_FLOAT;
    }
}
