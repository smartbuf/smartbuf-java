package com.github.sisyphsu.nakedata.node;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Inner ArrayList, don't copy Object[]
 *
 * @param <E>
 */
public class RefList<E> extends AbstractList<E> implements RandomAccess {

    private final int from;
    private final int size;
    private final E[] data;

    public RefList(E[] array, int size) {
        this.from = 0;
        this.size = size;
        this.data = Objects.requireNonNull(array);
    }

    public RefList(int from, int size, E[] data) {
        this.from = from;
        this.size = size;
        this.data = data;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Object[] toArray() {
        if (from == 0 && size == data.length) {
            return data;
        }
        Object[] result = new Object[size];
        System.arraycopy(data, from, result, 0, size);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        if (a.length < size) {
            a = (T[]) Array.newInstance(a.getClass().getComponentType(), size);
        }
        System.arraycopy(this.data, from, a, 0, size);
        return a;
    }

    @Override
    public E get(int index) {
        return data[index];
    }

    @Override
    public E set(int index, E element) {
        E oldValue = data[index];
        data[index] = element;
        return oldValue;
    }

    @Override
    public int indexOf(Object o) {
        for (int i = from; i < size; i++) {
            if (Objects.equals(o, this.data[i])) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Spliterator<E> spliterator() {
        return Spliterators.spliterator(data, Spliterator.ORDERED);
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        Objects.requireNonNull(action);
        for (E e : data) {
            action.accept(e);
        }
    }

    @Override
    public void replaceAll(UnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        E[] a = this.data;
        for (int i = from; i < size; i++) {
            a[i] = operator.apply(a[i]);
        }
    }

    @Override
    public void sort(Comparator<? super E> c) {
        Arrays.sort(data, c);
    }
}