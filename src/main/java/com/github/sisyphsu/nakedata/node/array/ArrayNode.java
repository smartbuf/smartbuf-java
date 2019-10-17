package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.ArrayType;
import com.github.sisyphsu.nakedata.DataType;
import com.github.sisyphsu.nakedata.node.Node;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Slice represent an subarray of ArrayNode.
 *
 * @author sulin
 * @since 2019-06-05 11:39:47
 */
public class ArrayNode extends Node {

    public static final ArrayNode NULL  = new ArrayNode(new ArrayList(), null);
    public static final ArrayNode EMPTY = new ArrayNode(new ArrayList(), null);

    protected final List      items;
    protected final ArrayType arrayType;

    protected ArrayNode(List items, ArrayType arrayType) {
        this.arrayType = arrayType;
        if (items == null) {
            throw new NullPointerException("items can't be null");
        }
        this.items = items;
    }

    public static ArrayNode nullArray(List nulls) {
        return new ArrayNode(nulls, ArrayType.NULL);
    }

    public static ArrayNode booleanArray(List<Boolean> booleans) {
        return new ArrayNode(booleans, ArrayType.BOOL);
    }

    public static ArrayNode byteArray(List<Byte> bytes) {
        return new ArrayNode(bytes, ArrayType.BYTE);
    }

    public static ArrayNode shortArray(List<Short> shorts) {
        return new ArrayNode(shorts, ArrayType.SHORT);
    }

    public static ArrayNode intArray(List<Integer> ints) {
        return new ArrayNode(ints, ArrayType.INT);
    }

    public static ArrayNode longArray(List<Long> longs) {
        return new ArrayNode(longs, ArrayType.LONG);
    }

    public static ArrayNode floatArray(List<Float> floats) {
        return new ArrayNode(floats, ArrayType.FLOAT);
    }

    public static ArrayNode doubleArray(List<Double> doubles) {
        return new ArrayNode(doubles, ArrayType.DOUBLE);
    }

    public static ArrayNode stringArray(List<String> strings) {
        return new ArrayNode(strings, ArrayType.STRING);
    }

    public static ArrayNode symbolArray(List<String> symbols) {
        return new ArrayNode(symbols, ArrayType.SYMBOL);
    }

    public static ArrayNode nodeArray(List<Node> nodes) {
        return new ArrayNode(nodes, ArrayType.OBJECT); // TODO 需要判断具体Node类型么？
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

    @Override
    public DataType dataType() {
        return DataType.ARRAY;
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

    /**
     * Inner ArrayList, don't copy Object[]
     *
     * @param <E>
     */
    public static class AsList<E> extends AbstractList<E> implements RandomAccess {

        private final E[] a;

        public AsList(E[] array) {
            a = Objects.requireNonNull(array);
        }

        @Override
        public int size() {
            return a.length;
        }

        @Override
        public Object[] toArray() {
            return a;
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T> T[] toArray(T[] a) {
            int size = size();
            if (a.length < size) {
                return Arrays.copyOf(this.a, size, (Class<? extends T[]>) a.getClass());
            }
            System.arraycopy(this.a, 0, a, 0, size);
            if (a.length > size) {
                a[size] = null;
            }
            return a;
        }

        @Override
        public E get(int index) {
            return a[index];
        }

        @Override
        public E set(int index, E element) {
            E oldValue = a[index];
            a[index] = element;
            return oldValue;
        }

        @Override
        public int indexOf(Object o) {
            E[] a = this.a;
            if (o == null) {
                for (int i = 0; i < a.length; i++)
                    if (a[i] == null)
                        return i;
            } else {
                for (int i = 0; i < a.length; i++)
                    if (o.equals(a[i]))
                        return i;
            }
            return -1;
        }

        @Override
        public boolean contains(Object o) {
            return indexOf(o) != -1;
        }

        @Override
        public Spliterator<E> spliterator() {
            return Spliterators.spliterator(a, Spliterator.ORDERED);
        }

        @Override
        public void forEach(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            for (E e : a) {
                action.accept(e);
            }
        }

        @Override
        public void replaceAll(UnaryOperator<E> operator) {
            Objects.requireNonNull(operator);
            E[] a = this.a;
            for (int i = 0; i < a.length; i++) {
                a[i] = operator.apply(a[i]);
            }
        }

        @Override
        public void sort(Comparator<? super E> c) {
            Arrays.sort(a, c);
        }
    }

}
