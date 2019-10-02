package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.Array;
import com.github.sisyphsu.nakedata.common.VarintArray;
import com.github.sisyphsu.nakedata.common.RecycleArray;
import com.github.sisyphsu.nakedata.common.IDAllocator;

/**
 * 需要为ObjectNode提供一种非常便利的struct-fields-id映射关系。
 * <p>
 * 搞一个全局的StructMap，为每个Struct分配一个唯一ID，这样做可以节省1ns的hashcode耗时
 *
 * @author sulin
 * @since 2019-09-26 21:32:54
 */
public final class OutputSchema {

    private final boolean enableCxt;

    private final Array<String>   tmpNames      = new Array<>(true);
    private final Array<String[]> tmpStructs    = new Array<>(true);
    private final Array<int[]>    tmpStructArea = new Array<>(true);

    private final int           cxtNameLimit    = 1 << 16;
    private final IDAllocator   nameIdAllocator = new IDAllocator();
    private       int[]         nameRefCounts   = new int[4];
    private final Array<String> names           = new Array<>(true);

    final Array<String>  nameAdded   = new Array<>(true);
    final Array<Integer> nameExpired = new Array<>(true);

    private final int                 cxtStructLimit = 1 << 12;
    private final Array<String[]>     cxtStructs     = new Array<>(true);
    private final RecycleArray<int[]> cxtStructArea  = new RecycleArray<>();

    final Array<int[]> structAdded   = new Array<>(true);
    final VarintArray  structExpired = new VarintArray(true);


    public OutputSchema(boolean enableCxt) {
        this.enableCxt = enableCxt;
    }

    /**
     * Reset for new round's output.
     */
    public void clear() {
        this.tmpNames.clear();
        this.tmpStructs.clear();
        this.tmpStructArea.clear();
        if (!enableCxt) {
            return;
        }
        this.nameExpired.clear();
        this.nameAdded.clear();
        this.structAdded.clear();
        this.structExpired.clear();
        // try release struct
        if (cxtStructArea.size() > cxtStructLimit) {
            long[] releasedItem = cxtStructArea.release(cxtStructLimit / 10);
            for (long l : releasedItem) {
                structExpired.add((int) l);
            }
            for (long item : releasedItem) {
                unreference(cxtStructArea.get((int) item));
            }
        }
        // try release name
        while (names.size() > cxtNameLimit) {
            long[] releasedItems = cxtStructArea.release(cxtStructLimit / 10);
            for (long l : releasedItems) {
                structExpired.add((int) l);
            }
            for (long item : releasedItems) {
                unreference(cxtStructArea.get((int) item));
            }
        }
    }

    /**
     * Add an struct into schema.
     */
    public void addTmpStruct(String[] fields) {
        if (tmpStructs.contains(fields)) {
            return;
        }
        int[] nameIds = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i];
            tmpNames.add(name);
            nameIds[i] = tmpNames.offset(name);
        }
        tmpStructArea.add(nameIds);
    }

    /**
     * Register struct for context sharing, could repeat.
     */
    public void addCxtStruct(String[] fields) {
        if (cxtStructs.contains(fields)) {
            return;
        }
        int[] nameIds = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            int nameId = registerName(fields[i]);
            nameIds[i] = nameId;
            nameRefCounts[nameId]++;
        }
        if (cxtStructArea.add(nameIds)) {
            structAdded.add(nameIds);
        }
    }

    public int findTmpStructID(String[] fields) {
        return tmpStructs.offset(fields);
    }

    public int findCxtStructID(String[] fields) {
        return cxtStructs.offset(fields);
    }

    private int registerName(String name) {
        Integer id = this.names.offset(name);
        if (id == null) {
            id = nameIdAllocator.acquire();
            if (id >= nameRefCounts.length) {
                int[] refCounts = new int[this.nameRefCounts.length];
                System.arraycopy(this.nameRefCounts, 0, refCounts, 0, this.nameRefCounts.length);
                this.nameRefCounts = refCounts;
            }
            nameRefCounts[id] = 0;
            names.set(id, name);
            nameAdded.add(name); // record nameAdded for context-sync.
        }
        return id;
    }

    private void unreference(int[] nameIds) {
        for (int nameId : nameIds) {
            int refCount = this.nameRefCounts[nameId];
            if (refCount > 1) {
                nameRefCounts[nameId] = refCount - 1;
            }
            // nameId should be released
            names.remove(nameId);
            nameIdAllocator.release(nameId);
            nameExpired.add(nameId);
        }
    }

}
