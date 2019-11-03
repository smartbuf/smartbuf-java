package com.github.sisyphsu.canoe.node;

import java.util.List;

/**
 * Base class of Node, and provider some common features.
 *
 * @author sulin
 * @since 2019-05-08 20:33:51
 */
public abstract class Node {

    public abstract Object value();

    public boolean booleanValue() {
        throw new UnsupportedOperationException();
    }

    public float floatValue() {
        throw new UnsupportedOperationException();
    }

    public double doubleValue() {
        throw new UnsupportedOperationException();
    }

    public long longValue() {
        throw new UnsupportedOperationException();
    }

    public String stringValue() {
        throw new UnsupportedOperationException();
    }

    public List<?> arrayValue() {
        throw new UnsupportedOperationException();
    }

    public boolean[] booleanArrayValue() {
        throw new UnsupportedOperationException();
    }

    public byte[] byteArrayValue() {
        throw new UnsupportedOperationException();
    }

    public short[] shortArrayValue() {
        throw new UnsupportedOperationException();
    }

    public int[] intArrayValue() {
        throw new UnsupportedOperationException();
    }

    public long[] longArrayValue() {
        throw new UnsupportedOperationException();
    }

    public float[] floatArrayValue() {
        throw new UnsupportedOperationException();
    }

    public double[] doubleArrayValue() {
        throw new UnsupportedOperationException();
    }

}
