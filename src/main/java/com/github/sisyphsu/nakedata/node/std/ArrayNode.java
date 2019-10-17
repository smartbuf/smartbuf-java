package com.github.sisyphsu.nakedata.node.std;

import com.github.sisyphsu.nakedata.ArrayType;
import com.github.sisyphsu.nakedata.NodeType;
import com.github.sisyphsu.nakedata.node.AsList;
import com.github.sisyphsu.nakedata.node.Node;

import java.util.List;
import java.util.function.Consumer;

/**
 * Slice represent an subarray of ArrayNode.
 *
 * @author sulin
 * @since 2019-06-05 11:39:47
 */
public class ArrayNode extends Node {

    public static final ArrayNode NULL  = new ArrayNode();
    public static final ArrayNode EMPTY = new ArrayNode();

    private int     size;
    private Slice[] slices = new Slice[1];

    public void addNullSlice(List nulls) {
        this.appendSlice(new Slice(nulls, ArrayType.NULL));
    }

    public void addBooleanSlice(List<Boolean> booleans) {
        this.appendSlice(new Slice(booleans, ArrayType.BOOL));
    }

    public void addByteSlice(List<Byte> bytes) {
        this.appendSlice(new Slice(bytes, ArrayType.BYTE));
    }

    public void addShortSlice(List<Short> shorts) {
        this.appendSlice(new Slice(shorts, ArrayType.SHORT));
    }

    public void addIntSlice(List<Integer> ints) {
        this.appendSlice(new Slice(ints, ArrayType.INT));
    }

    public void addLongSlice(List<Long> longs) {
        this.appendSlice(new Slice(longs, ArrayType.LONG));
    }

    public void addFloatSlice(List<Float> floats) {
        this.appendSlice(new Slice(floats, ArrayType.FLOAT));
    }

    public void addDoubleSlice(List<Double> doubles) {
        this.appendSlice(new Slice(doubles, ArrayType.DOUBLE));
    }

    public void addStringSlice(List<String> strings) {
        this.appendSlice(new Slice(strings, ArrayType.STRING));
    }

    public void addSymbolSlice(List<String> symbols) {
        this.appendSlice(new Slice(symbols, ArrayType.SYMBOL));
    }

    public void addNodeArray(List<Node> nodes) {
        this.appendSlice(new Slice(nodes, ArrayType.OBJECT)); // TODO 需要判断具体Node类型么？
    }

    @Override
    public NodeType dataType() {
        return NodeType.ARRAY;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

    void appendSlice(Slice slice) {
        if (size == slices.length - 1) {
            Slice[] tmp = new Slice[Math.max(4, slices.length * 2)];
            System.arraycopy(slices, 0, tmp, 0, size);
            this.slices = tmp;
        }
        this.slices[size++] = slice;
    }

    public List<Slice> getSlices() {
        return new AsList<>(slices, size);
    }

    public static class Slice {

        private final ArrayType type;
        private final List      items;

        Slice(List items, ArrayType type) {
            this.items = items;
            this.type = type;
        }

        /**
         * Get Array's element dataType
         *
         * @return DataType of element
         */
        public ArrayType elementType() {
            return type;
        }

        public List getItems() {
            return items;
        }

        public void forEach(Consumer<Object> consumer) {
            items.forEach(consumer);
        }
    }

}
