package com.github.sisyphsu.nakedata.context.common;

import java.util.HashMap;
import java.util.Map;

/**
 * FloatArray like float[], but support auto-scale and value-index.
 *
 * @author sulin
 * @since 2019-09-29 15:41:35
 */
public final class FloatArray {

    private int     offset;
    private float[] data = new float[4];

    private final Map<Float, Integer> indexMap;

    /**
     * Initialize with indexable.
     *
     * @param indexable Support value-index or not
     */
    public FloatArray(boolean indexable) {
        if (indexable) {
            this.indexMap = new HashMap<>();
        } else {
            this.indexMap = null;
        }
    }

    /**
     * Add new float into the final offset of this List
     *
     * @param val New float value
     */
    public void add(float val) {
        this.set(this.offset, val);
    }

    /**
     * Set new float item into the specified offset of this List
     *
     * @param pos The specified offset
     * @param val New float value
     */
    public void set(int pos, float val) {
        if (pos >= data.length) {
            float[] newItems = new float[data.length * 2];
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
     * Get float value from the specified offset
     *
     * @param offset The specified offset to fetch
     * @return Value at offsetd
     */
    public float get(int offset) {
        return data[offset];
    }

    /**
     * Remove float value at the specified offset, not support if indexable is false.
     * <p>
     * This operation will not delete offset for real, it only delete the associated index.
     *
     * @param offset The specified offset to remove
     */
    public void remove(int offset) {
        if (indexMap == null) {
            throw new UnsupportedOperationException();
        }
        float t = data[offset];
        indexMap.remove(t);
    }

    /**
     * Check this list contains the specified float of not, not support if indexable is false.
     *
     * @param val The specified float value
     * @return Contains or not
     */
    public boolean contains(float val) {
        if (indexMap == null) {
            throw new UnsupportedOperationException();
        }
        return indexMap.containsKey(val);
    }

    /**
     * Find the specified float value's position in this List, not support if indexable is false.
     *
     * @param val The specified float value
     * @return The array postion of val
     */
    public Integer offset(float val) {
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
     * Fetch the underline float[] of this List
     */
    public float[] data() {
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
