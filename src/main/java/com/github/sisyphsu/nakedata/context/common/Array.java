package com.github.sisyphsu.nakedata.context.common;

import java.util.HashMap;
import java.util.Map;

/**
 * ObjectArray like T[], but support auto-scale and value-index.
 *
 * @author sulin
 * @since 2019-09-26 14:21:44
 */
@SuppressWarnings("unchecked")
public final class Array<T> {

    private int      offset;
    private Object[] data = new Object[4];

    private final Map<T, Integer> indexMap;

    /**
     * Initialize with indexable.
     *
     * @param indexable Support value-index or not
     */
    public Array(boolean indexable) {
        if (indexable) {
            this.indexMap = new HashMap<>();
        } else {
            this.indexMap = null;
        }
    }

    /**
     * Add new object into the final offset of this List
     *
     * @param val New object of T
     */
    public void add(T val) {
        this.set(this.offset, val);
    }

    /**
     * Set new object into the specified offset of this List
     *
     * @param pos The specified offset
     * @param val New instance of T
     */
    public void set(int pos, T val) {
        if (val == null) {
            throw new NullPointerException("val cannot be null");
        }
        if (pos >= data.length) {
            Object[] newItems = new Object[data.length * 2];
            System.arraycopy(data, 0, newItems, 0, data.length);
            data = newItems;
        }
        if (pos >= this.offset) {
            this.offset = pos + 1;
        }
        if (indexMap != null && indexMap.put(val, pos) != null) {
            throw new IllegalArgumentException("value repeated: " + val);
        }
        this.data[pos] = val;
    }

    /**
     * Get T value from the specified offset
     *
     * @param offset The specified offset to fetch
     * @return Value at offsetd
     */
    public T get(int offset) {
        return (T) data[offset];
    }

    /**
     * Remove T value at the specified offset, not support if indexable is false.
     * <p>
     * This operation will not delete offset for real, it only delete the associated index.
     *
     * @param offset The specified offset to remove
     */
    public void remove(int offset) {
        if (indexMap == null) {
            throw new UnsupportedOperationException();
        }
        T t = (T) data[offset];
        indexMap.remove(t);
    }

    /**
     * Check this array contains the specified T of not, not support if indexable is false.
     *
     * @param val The specified T value
     * @return Contains or not
     */
    public boolean contains(T val) {
        if (indexMap == null) {
            throw new UnsupportedOperationException();
        }
        return indexMap.containsKey(val);
    }

    /**
     * Find the specified double value's position in this List, not support if indexable is false.
     *
     * @param val The specified double value
     * @return The array postion of val
     */
    public Integer offset(T val) {
        if (indexMap == null) {
            throw new UnsupportedOperationException();
        }
        return indexMap.get(val);
    }

    /**
     * Fetch real size of this Array
     */
    public int size() {
        if (indexMap != null) {
            return indexMap.size();
        } else {
            return offset;
        }
    }

    /**
     * Fetch the underline T[] of this Array
     */
    public T[] data() {
        return (T[]) data;
    }

    /**
     * Clear this List, reset all position
     */
    public void clear() {
        this.offset = 0;
        if (indexMap != null) {
            this.indexMap.clear();
        }
    }

}
