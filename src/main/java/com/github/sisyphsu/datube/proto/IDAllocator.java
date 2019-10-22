package com.github.sisyphsu.datube.proto;

import com.github.sisyphsu.datube.utils.ArrayUtils;

/**
 * allocate [0, max] id
 *
 * @author sulin
 * @since 2019-04-29 17:37:20
 */
public final class IDAllocator {

    /**
     * The next incremental id, if no reuseIds, it should be used at next time.
     */
    private int   nextId;
    /**
     * The real count of reuseIds.
     */
    private int   reuseCount;
    /**
     * The id was released.
     */
    private int[] reuseIds;

    /**
     * Acquire an unique and incremental id, if have released id, use it first.
     *
     * @return Unique and incremental id
     */
    public int acquire() {
        if (reuseCount == 0) {
            return nextId++;
        }
        return this.reuseIds[--reuseCount];
    }

    /**
     * Release the specified id, It will be used in high priority.
     *
     * @param id ID was released
     */
    public void release(int id) {
        if (id >= nextId) {
            throw new IllegalArgumentException(id + " is not acquired");
        }
        if (reuseIds == null) {
            reuseIds = new int[4];
        } else if (reuseCount >= reuseIds.length) {
            int[] tmp = new int[reuseIds.length * 2];
            System.arraycopy(reuseIds, 0, tmp, 0, reuseIds.length);
            this.reuseIds = tmp;
        }
        this.reuseIds[this.reuseCount] = id;
        ArrayUtils.descFastSort(this.reuseIds, 0, this.reuseCount);

        this.reuseCount++;
    }

}
