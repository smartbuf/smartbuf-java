package com.github.sisyphsu.datatube.transport;

import java.util.Objects;

/**
 * Array like T[], but support more features like auto-scale
 *
 * @author sulin
 * @since 2019-09-26 14:21:44
 */
@SuppressWarnings("unchecked")
public final class Array<T> {

    private int size;
    private T[] data;

    /**
     * Add new object into the final offset of this List
     *
     * @param val New object of T
     */
    public int add(T val) {
        int offset = this.size;
        this.put(offset, val);
        return offset;
    }

    /**
     * Put value into the specified offset of this List
     *
     * @param pos The specified offset
     * @param val New instance of T
     */
    public void put(int pos, T val) {
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

    /**
     * Get T value from the specified offset
     *
     * @param offset The specified offset to fetch
     * @return Value at offsetd
     */
    public T get(int offset) {
        return data[offset];
    }

    /**
     * Remove and return the last item of this array
     */
    public T popLast() {
        return data[--size];
    }

    /**
     * Return real size of this array
     */
    public int size() {
        return size;
    }

    /**
     * Return capacity of this array
     */
    public int cap() {
        return data == null ? 0 : data.length;
    }

    /**
     * Clear this array, don't need release reference, only reset position
     */
    public void clear() {
        size = 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Array)) {
            return false;
        }
        Array objArr = (Array) obj;
        if (objArr.size != size) {
            return false;
        }
        for (int i = 0; i < size; i++) {
            if (!Objects.deepEquals(data[i], objArr.data[i])) {
                return false;
            }
        }
        return true;
    }

}
