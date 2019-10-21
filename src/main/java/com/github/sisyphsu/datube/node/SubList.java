package com.github.sisyphsu.datube.node;

import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Comparator;
import java.util.Objects;
import java.util.RandomAccess;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * SubList helps wrap Object[] as List, and don't need array copy.
 *
 * @author sulin
 * @since 2019-05-08 20:25:32
 */
public final class SubList<E> extends AbstractList<E> implements RandomAccess {

    private final int from;
    private final int to;
    private final E[] data;

    public SubList(int from, int to, E[] data) {
        this.from = from;
        this.to = to;
        this.data = data;
    }

    @Override
    public int size() {
        return to - from;
    }

    @Override
    public Object[] toArray() {
        if (from == 0 && to == data.length) {
            return data;
        }
        Object[] result = new Object[size()];
        System.arraycopy(data, from, result, 0, size());
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size()) {
            a = (T[]) Array.newInstance(a.getClass().getComponentType(), size());
        }
        System.arraycopy(this.data, from, a, 0, size());
        return a;
    }

    @Override
    public E get(int index) {
        return data[from + index];
    }

    @Override
    public E set(int index, E element) {
        int off = from + index;
        E oldValue = data[off];
        data[off] = element;
        return oldValue;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = from; i < to; i++) {
            if (Objects.equals(o, this.data[i])) {
                return i - from;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        for (int i = from; i < to; i++) {
            action.accept(data[i]);
        }
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sort(Comparator<? super E> c) {
        throw new UnsupportedOperationException();
    }
}
