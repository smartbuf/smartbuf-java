package com.github.sisyphsu.nakedata.node.std;

import com.github.sisyphsu.nakedata.ArrayType;
import com.github.sisyphsu.nakedata.node.Node;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author sulin
 * @since 2019-10-17 17:48:13
 */
public final class SliceNode {

    private final List      items;
    private final ArrayType arrayType;

    protected SliceNode(List items, ArrayType arrayType) {
        this.arrayType = arrayType;
        if (items == null) {
            throw new NullPointerException("items can't be null");
        }
        this.items = items;
    }

    public static SliceNode nullArray(List nulls) {
        return new SliceNode(nulls, ArrayType.NULL);
    }

    public static SliceNode booleanArray(List<Boolean> booleans) {
        return new SliceNode(booleans, ArrayType.BOOL);
    }

    public static SliceNode byteArray(List<Byte> bytes) {
        return new SliceNode(bytes, ArrayType.BYTE);
    }

    public static SliceNode shortArray(List<Short> shorts) {
        return new SliceNode(shorts, ArrayType.SHORT);
    }

    public static SliceNode intArray(List<Integer> ints) {
        return new SliceNode(ints, ArrayType.INT);
    }

    public static SliceNode longArray(List<Long> longs) {
        return new SliceNode(longs, ArrayType.LONG);
    }

    public static SliceNode floatArray(List<Float> floats) {
        return new SliceNode(floats, ArrayType.FLOAT);
    }

    public static SliceNode doubleArray(List<Double> doubles) {
        return new SliceNode(doubles, ArrayType.DOUBLE);
    }

    public static SliceNode stringArray(List<String> strings) {
        return new SliceNode(strings, ArrayType.STRING);
    }

    public static SliceNode symbolArray(List<String> symbols) {
        return new SliceNode(symbols, ArrayType.SYMBOL);
    }

    public static SliceNode nodeArray(List<Node> nodes) {
        return new SliceNode(nodes, ArrayType.OBJECT); // TODO 需要判断具体Node类型么？
    }

    /**
     * Get Array's element dataType
     *
     * @return DataType of element
     */
    public ArrayType elementType() {
        return arrayType;
    }

    public List getItems() {
        return items;
    }

    public void forEach(Consumer<Object> consumer) {
        items.forEach(consumer);
    }

}
