package com.github.sisyphsu.nakedata.context.output;

/**
 * Array is an simple array implementation which support auto-expansion.
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
final class Array<T> {

    int size;
    private T[] data;

    int add(T val) {
        int offset = this.size;
        this.put(offset, val);
        return offset;
    }

    void put(int pos, T val) {
        if (data == null) {
            data = (T[]) new Object[4];
        }
        if (pos >= data.length) {
            T[] newArr = (T[]) new Object[data.length * 2];
            System.arraycopy(data, 0, newArr, 0, data.length);
            data = newArr;
        }
        if (pos >= this.size) {
            size = pos + 1;
        }
        data[pos] = val;
    }

    T get(int offset) {
        return data[offset];
    }

}
