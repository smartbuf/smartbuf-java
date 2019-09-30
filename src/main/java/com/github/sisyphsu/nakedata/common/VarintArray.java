package com.github.sisyphsu.nakedata.common;

import java.util.HashMap;
import java.util.Map;

/**
 * LongArray like long[], but support auto-scale and value-index.
 *
 * @author sulin
 * @since 2019-09-29 15:41:35
 */
public final class VarintArray {

    private int    offset;
    private long[] data = new long[4];

    private final Map<Long, Integer> indexMap;

    /**
     * Initialize with indexable.
     *
     * @param indexable Support value-index or not
     */
    public VarintArray(boolean indexable) {
        if (indexable) {
            this.indexMap = new HashMap<>();
        } else {
            this.indexMap = null;
        }
    }

    /**
     * Add new long into the final offset of this List
     *
     * @param val New long value
     */
    public void add(long val) {
        this.add(this.offset, val);
    }

    /**
     * Set new long item into the specified offset of this List
     *
     * @param pos The specified offset
     * @param val New long value
     */
    public void add(int pos, long val) {
        if (pos >= data.length) {
            long[] newItems = new long[data.length * 2];
            System.arraycopy(data, 0, newItems, 0, data.length);
            data = newItems;
        }
        if (this.offset <= pos) {
            this.offset = pos + 1;
        }
        if (indexMap != null && indexMap.put(val, pos) != null) {
            throw new IllegalArgumentException("value repeated: " + val);
        }
        this.data[pos] = val;
    }

    /**
     * Get long value from the specified offset
     *
     * @param offset The specified offset to fetch
     * @return Value at offsetd
     */
    public long get(int offset) {
        return data[offset];
    }

    /**
     * Remove long value at the specified offset, not support if indexable is false.
     * <p>
     * This operation will not delete offset for real, it only delete the associated index.
     *
     * @param offset The specified offset to remove
     */
    public void remove(int offset) {
        if (indexMap == null) {
            throw new UnsupportedOperationException();
        }
        long t = data[offset];
        indexMap.remove(t);
    }

    /**
     * Check this list contains the specified long of not, not support if indexable is false.
     *
     * @param val The specified long value
     * @return Contains or not
     */
    public boolean contains(long val) {
        if (indexMap == null) {
            throw new UnsupportedOperationException();
        }
        return indexMap.containsKey(val);
    }

    /**
     * Find the specified long value's position in this List, not support if indexable is false.
     *
     * @param val The specified long value
     * @return The array postion of val
     */
    public Integer offset(long val) {
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
     * Fetch the underline long[] of this List
     */
    public long[] data() {
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
