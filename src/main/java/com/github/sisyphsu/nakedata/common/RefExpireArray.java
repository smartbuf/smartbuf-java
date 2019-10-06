package com.github.sisyphsu.nakedata.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Array that record reference times, and release useless item automatically.
 *
 * @author sulin
 * @since 2019-10-06 14:36:40
 */
@SuppressWarnings("unchecked")
public class RefExpireArray<T> {

    private final IDAllocator     idAllocator = new IDAllocator();
    private final Map<T, Integer> indexMap    = new HashMap<>();

    private int[]    counts = new int[4];
    private Object[] items  = new Object[4];

    /**
     * Add item into this array, and increase its ref-count if exist
     *
     * @param t Item
     * @return It's new or not
     */
    public boolean add(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        Integer offset = indexMap.get(t);
        boolean isNew = false;
        if (offset == null) {
            offset = idAllocator.acquire();
            if (offset >= items.length) {
                int len = items.length;
                Object[] newItems = new Object[len * 2];
                System.arraycopy(items, 0, newItems, 0, len);
                this.items = newItems;
                int[] newCounts = new int[len * 2];
                System.arraycopy(counts, 0, newCounts, 0, len);
                this.counts = newCounts;
            }
            this.items[offset] = t;
            this.indexMap.put(t, offset);
            this.counts[offset] = 1;
            isNew = true;
        } else {
            this.counts[offset]++;
        }
        return isNew;
    }

    /**
     * Get the specified item by its offset
     *
     * @param offset Item's offset in array
     */
    public T get(int offset) {
        return (T) items[offset];
    }

    /**
     * Get offset of the specified item in array
     *
     * @param t Item
     */
    public int offset(T t) {
        return indexMap.get(t);
    }

    /**
     * Check this array contains the specified item or not
     *
     * @param t The specified item
     * @return This Array contains it or not
     */
    public boolean contains(T t) {
        return indexMap.containsKey(t);
    }

    /**
     * Get the real size of this Array
     *
     * @return Real size
     */
    public int size() {
        return indexMap.size();
    }

    /**
     * Mark unreference items by its offset.
     *
     * @param offsets The offset of items
     */
    public void unreference(int... offsets) {
        for (int offset : offsets) {
            int count = this.counts[offset];
            if (count > 1) {
                this.counts[offset] = count - 1;
                continue;
            }
            T t = (T) this.items[offset];
            if (t == null) {
                throw new IllegalStateException("unreference should exists");
            }
            this.items[offset] = null;
            this.indexMap.remove(t);
            this.idAllocator.release(offset);
        }
    }

}
