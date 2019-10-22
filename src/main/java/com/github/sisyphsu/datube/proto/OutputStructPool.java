package com.github.sisyphsu.datube.proto;

import com.github.sisyphsu.datube.utils.ArrayUtils;
import com.github.sisyphsu.datube.utils.TimeUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * StructPool represents an area holds struct for sharing, which support temporary and context using.
 * <p>
 * It will allocate unique ID for every struct by its fields.
 *
 * @author sulin
 * @since 2019-10-07 21:29:36
 */
public final class OutputStructPool {

    final Array<Struct>  tmpStructs     = new Array<>();
    final Array<Struct>  cxtStructAdded = new Array<>();
    final Array<Integer> cxtStructExpired;

    private final int                 cxtLimit;
    private final IDAllocator         cxtIdAlloc = new IDAllocator();
    private final Array<Struct>       cxtStructs = new Array<>();
    private final Map<Struct, Struct> index      = new HashMap<>();

    private final Struct reuseKey = new Struct(false, 0, 0, null);

    /**
     * Initialize StructPool with the specified limit of context-struct
     *
     * @param limit Max number of context-struct
     */
    public OutputStructPool(Schema schema, int limit) {
        this.cxtLimit = limit;
        this.cxtStructExpired = schema.cxtStructExpired;
    }

    /**
     * Register the specified struct into pool by its field-names.
     *
     * @param temporary It's temporary or not
     * @param names     FieldNames which represents an struct
     */
    public void register(boolean temporary, String[] names) {
        if (names == null) {
            throw new NullPointerException();
        }
        if (names.length == 0) {
            return;
        }
        int now = (int) TimeUtils.fastUpTime();
        Struct struct = index.get(reuseKey.wrap(names));
        if (struct != null) {
            if (temporary || !struct.temporary) {
                struct.lastTime = now;
                return;
            }
            Struct lastStruct = tmpStructs.popLast();
            if (lastStruct != struct) {
                lastStruct.offset = struct.offset;
                tmpStructs.put(struct.offset, lastStruct);
            }
            this.index.remove(struct);
        }
        if (temporary) {
            struct = new Struct(true, tmpStructs.size(), now, names);
            this.tmpStructs.add(struct);
        } else {
            int offset = cxtIdAlloc.acquire();
            struct = new Struct(false, offset, now, names);
            this.cxtStructs.put(offset, struct);
            this.cxtStructAdded.add(struct);
        }
        this.index.put(struct, struct);
    }

    /**
     * Find unique id of the specified struct by its field-names
     *
     * @param fields field-names which represents an struct
     * @return Its unique id
     */
    public int findStructID(String[] fields) {
        if (fields.length == 0) {
            return 0;
        }
        Struct struct = index.get(reuseKey.wrap(fields));
        if (struct == null) {
            throw new IllegalArgumentException("struct not exists: " + Arrays.toString(fields));
        }
        if (struct.temporary) {
            return 1 + struct.offset;
        }
        return 1 + tmpStructs.size() + struct.offset;
    }

    /**
     * Fetch total number of struct in temporary and context area.
     *
     * @return Total number
     */
    public int size() {
        return index.size();
    }

    /**
     * Reset this struct pool, and execute struct-expire automatically
     */
    public void reset() {
        for (int i = 0, len = tmpStructs.size(); i < len; i++) {
            index.remove(tmpStructs.get(i));
        }
        this.tmpStructs.clear();
        this.cxtStructAdded.clear();

        // execute automatically expire for context-struct
        int expireCount = index.size() - cxtLimit;
        if (expireCount <= 0) {
            return;
        }
        int heapStatus = 0; // 0 means init, 1 means stable, -1 means not-stable.
        int heapOffset = 0;
        long[] heap = new long[expireCount];
        for (int i = 0, size = cxtStructs.cap(); i < size; i++) {
            if (cxtStructs.get(i) == null) {
                continue;
            }
            int itemTime = cxtStructs.get(i).lastTime;
            int itemOffset = cxtStructs.get(i).offset;
            if (heapOffset < expireCount) {
                heap[heapOffset++] = ((long) itemTime) << 32 | (long) itemOffset;
                continue;
            }
            if (heapStatus == 0) {
                ArrayUtils.descFastSort(heap, 0, expireCount - 1); // sort by activeTime, heap[0] has biggest activeTime
                heapStatus = 1;
            } else if (heapStatus == -1) {
                ArrayUtils.maxHeapAdjust(heap, 0, expireCount); // make sure heap[0] has biggest activeTime
                heapStatus = 1;
            }
            if (itemTime > (int) (heap[0] >>> 32)) {
                continue; // item is newer than all items in heap
            }
            heap[0] = ((long) itemTime) << 32 | (long) itemOffset;
            heapStatus = -1;
        }
        for (long l : heap) {
            Struct expiredStruct = cxtStructs.get((int) (l));
            index.remove(expiredStruct);
            cxtIdAlloc.release(expiredStruct.offset);
            cxtStructs.put(expiredStruct.offset, null);
            this.cxtStructExpired.add(expiredStruct.offset);
        }
    }

    /**
     * Struct model for inner usage
     */
    static final class Struct {
        boolean  temporary;
        int      offset;
        int      lastTime;
        String[] names;

        public Struct(boolean temporary, int offset, int lastTime, String[] names) {
            this.temporary = temporary;
            this.offset = offset;
            this.lastTime = lastTime;
            this.names = names;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(names);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Struct) {
                return Arrays.equals(names, ((Struct) obj).names);
            }
            return false;
        }

        Struct wrap(String[] names) {
            this.names = names;
            return this;
        }
    }

}
