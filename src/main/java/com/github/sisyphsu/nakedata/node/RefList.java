package com.github.sisyphsu.nakedata.node;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

/**
 * Inner ArrayList, don't copy Object[]
 *
 * @param <E>
 */
public class RefList<E> extends AbstractList<E> implements RandomAccess {

    private final int fromOff;
    private final int toOff;
    private final E[] data;

    public RefList(E[] array, int toOff) {
        this.fromOff = 0;
        this.toOff = toOff;
        this.data = Objects.requireNonNull(array);
    }

    public RefList(int fromOff, int toOff, E[] data) {
        this.fromOff = fromOff;
        this.toOff = toOff;
        this.data = data;
    }

    @Override
    public int size() {
        return toOff;
    }

    @Override
    public Object[] toArray() {
        return data;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
        int size = size();
        if (a.length < size) {
            return Arrays.copyOf(this.data, size, (Class<? extends T[]>) a.getClass());
        }
        System.arraycopy(this.data, 0, a, 0, size);
        if (a.length > size) {
            a[size] = null;
        }
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
        final E[] a = this.data;
        if (o == null) {
            for (int i = 0; i < toOff; i++)
                if (a[i] == null)
                    return i;
        } else {
            for (int i = 0; i < toOff; i++)
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
        for (int i = 0; i < toOff; i++) {
            a[i] = operator.apply(a[i]);
        }
    }

    @Override
    public void sort(Comparator<? super E> c) {
        Arrays.sort(data, c);
    }
}
