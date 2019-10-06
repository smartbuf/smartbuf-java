package com.github.sisyphsu.nakedata.context.common;

import java.util.HashMap;
import java.util.Map;

/**
 * DoubleArray like double[], but support auto-scale and value-index.
 *
 * @author sulin
 * @since 2019-09-29 15:41:35
 */
public final class DoubleArray {

    private int      offset;
    private double[] data = new double[4];

    private final Map<Double, Integer> indexMap;

    /**
     * Initialize with indexable.
     *
     * @param indexable Support value-index or not
     */
    public DoubleArray(boolean indexable) {
        if (indexable) {
            this.indexMap = new HashMap<>();
        } else {
            this.indexMap = null;
        }
    }

    /**
     * Add new double into the final offset of this List
     *
     * @param val New double value
     */
    public void add(double val) {
        this.set(this.offset, val);
    }

    /**
     * Set new double item into the specified offset of this List
     *
     * @param pos The specified offset
     * @param val New double value
     */
    public void set(int pos, double val) {
        if (pos >= data.length) {
            double[] newItems = new double[data.length * 2];
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
     * Get double value from the specified offset
     *
     * @param offset The specified offset to fetch
     * @return Value at offsetd
     */
    public double get(int offset) {
        return data[offset];
    }

    /**
     * Remove double value at the specified offset, not support if indexable is false.
     * <p>
     * This operation will not delete offset for real, it only delete the associated index.
     *
     * @param offset The specified offset to remove
     */
    public void remove(int offset) {
        if (indexMap == null) {
            throw new UnsupportedOperationException();
        }
        double t = data[offset];
        indexMap.remove(t);
    }

    /**
     * Check this list contains the specified double of not, not support if indexable is false.
     *
     * @param val The specified double value
     * @return Contains or not
     */
    public boolean contains(double val) {
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
    public Integer offset(double val) {
        if (indexMap == null) {
            throw new UnsupportedOperationException();
        }
        return indexMap.get(val);
    }

    /**
     * Fetch real size of this List
     */
    public int size() {
        if (indexMap != null) {
            return indexMap.size();
        } else {
            return offset;
        }
    }

    /**
     * Fetch the underline double[] of this List
     */
    public double[] data() {
        return data;
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
