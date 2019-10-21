package com.github.sisyphsu.datube.node.std;

import com.github.sisyphsu.datube.node.NodeType;
import com.github.sisyphsu.datube.node.SliceType;
import com.github.sisyphsu.datube.node.Node;

import java.util.List;

/**
 * ArrayNode represents all kinds of primary array or Object[].
 *
 * @author sulin
 * @since 2019-06-05 11:39:47
 */
@SuppressWarnings("unchecked")
public final class ArrayNode extends Node {

    public static final ArrayNode EMPTY = new ArrayNode();

    private int     size;
    private Slice[] slices = new Slice[1];

    public static ArrayNode valueOf(boolean[] booleans) {
        return new ArrayNode().appendSlice(booleans, booleans.length, SliceType.BOOL_NATIVE);
    }

    public static ArrayNode valueOf(byte[] bytes) {
        return new ArrayNode().appendSlice(bytes, bytes.length, SliceType.BYTE_NATIVE);
    }

    public static ArrayNode valueOf(short[] shorts) {
        return new ArrayNode().appendSlice(shorts, shorts.length, SliceType.SHORT_NATIVE);
    }

    public static ArrayNode valueOf(int[] ints) {
        return new ArrayNode().appendSlice(ints, ints.length, SliceType.INT_NATIVE);
    }

    public static ArrayNode valueOf(long[] longs) {
        return new ArrayNode().appendSlice(longs, longs.length, SliceType.LONG_NATIVE);
    }

    public static ArrayNode valueOf(float[] floats) {
        return new ArrayNode().appendSlice(floats, floats.length, SliceType.FLOAT_NATIVE);
    }

    public static ArrayNode valueOf(double[] doubles) {
        return new ArrayNode().appendSlice(doubles, doubles.length, SliceType.DOUBLE_NATIVE);
    }

    public void addNullSlice(List nulls) {
        this.appendSlice(nulls, nulls.size(), SliceType.NULL);
    }

    public void addBooleanSlice(List<Boolean> booleans) {
        this.appendSlice(booleans, booleans.size(), SliceType.BOOL);
    }

    public void addByteSlice(List<Byte> bytes) {
        this.appendSlice(bytes, bytes.size(), SliceType.BYTE);
    }

    public void addShortSlice(List<Short> shorts) {
        this.appendSlice(shorts, shorts.size(), SliceType.SHORT);
    }

    public void addIntSlice(List<Integer> ints) {
        this.appendSlice(ints, ints.size(), SliceType.INT);
    }

    public void addLongSlice(List<Long> longs) {
        this.appendSlice(longs, longs.size(), SliceType.LONG);
    }

    public void addFloatSlice(List<Float> floats) {
        this.appendSlice(floats, floats.size(), SliceType.FLOAT);
    }

    public void addDoubleSlice(List<Double> doubles) {
        this.appendSlice(doubles, doubles.size(), SliceType.DOUBLE);
    }

    public void addStringSlice(List<String> strings) {
        this.appendSlice(strings, strings.size(), SliceType.STRING);
    }

    public void addSymbolSlice(List<String> symbols) {
        this.appendSlice(symbols, symbols.size(), SliceType.SYMBOL);
    }

    public void addArraySlice(List<ArrayNode> nodes) {
        this.appendSlice(nodes, nodes.size(), SliceType.ARRAY);
    }

    public void addObjectSlice(List<ObjectNode> nodes) {
        this.appendSlice(nodes, nodes.size(), SliceType.OBJECT);
    }

    public ArrayNode appendSlice(Object obj, int sliceSize, SliceType type) {
        Slice slice = new Slice(obj, sliceSize, type);
        if (size == slices.length - 1) {
            Slice[] tmp = new Slice[Math.max(4, slices.length * 2)];
            System.arraycopy(slices, 0, tmp, 0, size);
            this.slices = tmp;
        }
        this.slices[size++] = slice;
        return this;
    }

    @Override
    public NodeType type() {
        return NodeType.ARRAY;
    }

    public int size() {
        return size;
    }

    public Slice[] slices() {
        return slices;
    }

    public static class Slice {

        private final Object    data;
        private final int       size;
        private final SliceType type;

        private Slice(Object data, int size, SliceType type) {
            this.data = data;
            this.size = size;
            this.type = type;
        }

        public SliceType elementType() {
            return type;
        }

        public int size() {
            return size;
        }

        public Object data() {
            return data;
        }

        public boolean[] asBooleanArray() {
            return (boolean[]) data;
        }

        public byte[] asByteArray() {
            return (byte[]) data;
        }

        public short[] asShortArray() {
            return (short[]) data;
        }

        public int[] asIntArray() {
            return (int[]) data;
        }

        public long[] asLongArray() {
            return (long[]) data;
        }

        public float[] asFloatArray() {
            return (float[]) data;
        }

        public double[] asDoubleArray() {
            return (double[]) data;
        }

        public <T> List<T> asList() {
            return (List<T>) data;
        }

        public List<Boolean> asBoolSlice() {
            return (List<Boolean>) data;
        }

        public List<Byte> asByteSlice() {
            return (List<Byte>) data;
        }

        public List<Short> asShortSlice() {
            return (List<Short>) data;
        }

        public List<Integer> asIntSlice() {
            return (List<Integer>) data;
        }

        public List<Long> asLongSlice() {
            return (List<Long>) data;
        }

        public List<Float> asFloatSlice() {
            return (List<Float>) data;
        }

        public List<Double> asDoubleSlice() {
            return (List<Double>) data;
        }

        public List<String> asStringSlice() {
            return (List<String>) data;
        }

        public List<String> asSymbolSlice() {
            return (List<String>) data;
        }

        public List<ObjectNode> asObjectSlice() {
            return (List<ObjectNode>) data;
        }

        public List<ArrayNode> asArraySlice() {
            return (List<ArrayNode>) data;
        }
    }

}
