package com.github.sisyphsu.nakedata.context.common;

import com.github.sisyphsu.nakedata.utils.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Array that support recycling.
 *
 * @author sulin
 * @since 2019-09-25 20:37:51
 */
@SuppressWarnings("unchecked")
public final class TimeExpireArray<T> {

    private static final long INIT_TIME = System.currentTimeMillis();

    private int[]    activeTimes = new int[4];
    private Object[] data        = new Object[4];

    private final IDAllocator     idAllocator = new IDAllocator();
    private final Map<T, Integer> indexMap    = new HashMap<>();

    /**
     * Add new item into this array.
     *
     * @param t New item
     * @return t is new or not
     */
    public boolean add(T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        int now = (int) ((System.currentTimeMillis() - INIT_TIME));
        boolean isNew = false;
        Integer offset = indexMap.get(t);
        if (offset == null) {
            offset = idAllocator.acquire();
            if (offset >= data.length) {
                int len = data.length;
                Object[] newItems = new Object[len * 2];
                System.arraycopy(data, 0, newItems, 0, len);
                data = newItems;
                int[] tmp = new int[len * 2];
                System.arraycopy(activeTimes, 0, tmp, 0, len);
                activeTimes = tmp;
            }
            this.data[offset] = t;
            this.indexMap.put(t, offset);
            isNew = true;
        }
        activeTimes[offset] = now;
        return isNew;
    }

    /**
     * Get the specified data by id(offset).
     *
     * @param offset Item's id
     */
    public T get(int offset) {
        return (T) data[offset];
    }

    /**
     * Find unique id by item.
     *
     * @param t Item
     * @return ID
     */
    public int offset(T t) {
        return indexMap.get(t);
    }

    /**
     * Check this array contains t or not.
     *
     * @param t Item
     * @return This array contains t or not
     */
    public boolean contains(T t) {
        return indexMap.containsKey(t);
    }

    /**
     * Get the real size of this Area, which means item's count.
     *
     * @return item's count
     */
    public int size() {
        return indexMap.size();
    }

    /**
     * Release the specified number of items which are inactive for long time.
     *
     * @param count Max number to release.
     * @return The id of items which were released.
     */
    public long[] release(int count) {
        long[] heap = new long[count];

        // 0 means init, 1 means stable, -1 means not-stable.
        int heapStatus = 0;
        T[] items = (T[]) data;
        int itemCount = idAllocator.count();
        for (int itemOffset = 0, heapOffset = 0; itemOffset < itemCount; itemOffset++) {
            if (items[itemOffset] == null) {
                continue;
            }
            int itemTime = activeTimes[itemOffset];
            if (heapOffset < count) {
                heap[heapOffset++] = ((long) itemTime) << 32 | (long) itemOffset;
                continue;
            }
            if (heapStatus == 0) {
                ArrayUtils.descFastSort(heap, 0, count - 1); // sort by activeTime, heap[0] has biggest activeTime
                heapStatus = 1;
            } else if (heapStatus == -1) {
                ArrayUtils.maxHeapAdjust(heap, 0, count); // make sure heap[0] has biggest activeTime
                heapStatus = 1;
            }
            if (itemTime > (int) (heap[0] >>> 32)) {
                continue; // item is newer than all items in heap
            }
            heap[0] = ((long) itemTime) << 32 | (long) itemOffset;
            heapStatus = -1;
        }

        // release all items in heap
        for (long l : heap) {
            int off = (int) (l);
            idAllocator.release(off);
            indexMap.remove(items[off]);
            items[off] = null;
        }
        return heap;
    }

}
