package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.utils.IDPool;

import java.util.ArrayList;
import java.util.List;

/**
 * 支持add，同时根据活跃度清理旧的数据
 *
 * @author sulin
 * @since 2019-09-25 20:37:51
 */
public final class OutputContextPool<T> {

    private static final long INIT_TIME = System.currentTimeMillis();

    private final IDPool        itemIdPool      = new IDPool();
    private final CxtList<T>    itemList        = new CxtList<>();
    private final List<Integer> itemActiveTimes = new ArrayList<>();

    final List<T>       itemAdded   = new ArrayList<>();
    final List<Integer> itemExpired = new ArrayList<>();

    public void add(T t) {
        int now = (int) ((System.currentTimeMillis() - INIT_TIME));
        Integer offset = itemList.getID(t);
        if (offset == null) {
            offset = itemIdPool.acquire();
            itemList.add(offset, t);
            itemAdded.add(t);
            if (itemActiveTimes.size() <= offset) {
                itemActiveTimes.add(now);
            } else {
                itemActiveTimes.set(offset, now);
            }
        } else {
            itemActiveTimes.set(offset, now);
        }
    }

    /**
     * Get the specified data by id(offset).
     *
     * @param id Item's id
     */
    public T get(int id) {
        return itemList.get(id);
    }

    /**
     * Find unique id by item.
     *
     * @param t Item
     * @return ID
     */
    public int findID(T t) {
        return itemList.getID(t);
    }

    /**
     * Get the real size of this Area, which means item's count.
     *
     * @return item's count
     */
    public int size() {
        return itemList.size();
    }

    public void resetContext() {
        this.itemAdded.clear();
        this.itemExpired.clear();
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
        int itemSize = itemList.size();
        T[] items = itemList.data();
        for (int itemId = 0; itemId < itemSize; itemId++) {
            if (items[itemId] == null) {
                continue;
            }
            int itemTime = itemActiveTimes.get(itemId);
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
            itemIdPool.release(id);
            itemExpired.add(id);
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
