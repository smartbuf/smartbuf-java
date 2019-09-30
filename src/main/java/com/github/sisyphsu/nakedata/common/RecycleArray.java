package com.github.sisyphsu.nakedata.common;

/**
 * Array that support recycling.
 *
 * @author sulin
 * @since 2019-09-25 20:37:51
 */
public final class RecycleArray<T> {

    private static final long INIT_TIME = System.currentTimeMillis();

    private int[] activeTimes = new int[4];

    private final IDAllocator idAllocator = new IDAllocator();
    private final Array<T>    array       = new Array<>(true);

    /**
     * Add new item into this array.
     *
     * @param t New item
     * @return Added or not
     */
    public boolean add(T t) {
        int now = (int) ((System.currentTimeMillis() - INIT_TIME));
        Integer offset = array.offset(t);
        boolean isNew = false;
        if (offset == null) {
            offset = idAllocator.acquire();
            array.add(offset, t);
            if (offset >= activeTimes.length) {
                int[] tmp = new int[activeTimes.length * 2];
                System.arraycopy(activeTimes, 0, tmp, 0, activeTimes.length);
                activeTimes = tmp;
            }
            isNew = true;
        }
        activeTimes[offset] = now;
        return isNew;
    }

    /**
     * Get the specified data by id(offset).
     *
     * @param id Item's id
     */
    public T get(int id) {
        return array.get(id);
    }

    /**
     * Find unique id by item.
     *
     * @param t Item
     * @return ID
     */
    public int offset(T t) {
        return array.offset(t);
    }

    /**
     * Get the real size of this Area, which means item's count.
     *
     * @return item's count
     */
    public int size() {
        return array.size();
    }

    /**
     * Release the specified number of items which are inactive for long time.
     *
     * @param count Max number to release.
     * @return The id of items which were released.
     */
    public long[] release(int count) {
        int offset = 0;
        long[] heap = new long[count];

        // 0 means init, 1 means stable, -1 means not-stable.
        int heapStatus = 0;
        int itemSize = array.size();
        T[] items = array.data();
        for (int itemId = 0; itemId < itemSize; itemId++) {
            if (items[itemId] == null) {
                continue;
            }
            int itemTime = activeTimes[itemId];
            if (offset < count) {
                heap[offset++] = ((long) itemTime) << 32 | itemId;
                continue;
            }
            if (heapStatus == 0) {
                for (int i = count / 2; i >= 0; i--) {
                    this.headAdjust(heap, i, count); // adjuest all
                }
                heapStatus = 1;
            } else if (heapStatus == -1) {
                this.headAdjust(heap, 0, count); // max-heap adjust, make sure heap[0] has smallest time
                heapStatus = 1;
            }
            if (itemTime > (int) (heap[0] >>> 32)) {
                continue; // item is newer than all items in heap
            }
            heap[0] = ((long) itemTime) << 32 | itemId;
            heapStatus = -1;
        }

        // release all items in heap
        for (long l : heap) {
            int id = (int) (l);
            idAllocator.release(id);
            array.remove(id);
        }
        return heap;
    }

    // Adjust the specified heap from root, make sure the largest item at first.
    private void headAdjust(long[] heap, int root, int len) {
        int parent = root;
        int child = 2 * parent + 1;
        for (; child < len; parent = child, child = 2 * parent + 1) {
            // select the bigger child
            if (child + 1 < len && heap[child] < heap[child + 1]) {
                child = child + 1;
            }
            // break on stable heap
            if (heap[parent] >= heap[child]) {
                break;
            }
            // exchange father and child
            long tmp = heap[child];
            heap[child] = heap[parent];
            heap[parent] = tmp;
        }
    }

}
