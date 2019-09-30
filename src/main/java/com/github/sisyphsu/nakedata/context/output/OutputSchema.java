package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.ObjectArray;
import com.github.sisyphsu.nakedata.utils.IDPool;

import java.util.ArrayList;
import java.util.List;

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

    private final ObjectArray<String>   tmpNames      = new ObjectArray<>(true);
    private final ObjectArray<String[]> tmpStructs    = new ObjectArray<>(true);
    private final ObjectArray<int[]>    tmpStructArea = new ObjectArray<>(true);

    private final int                 cxtNameLimit  = 1 << 16;
    private final IDPool              nameIdPool    = new IDPool();
    private       int[]               nameRefCounts = new int[4];
    private final ObjectArray<String> names         = new ObjectArray<>(true);

    private final int                   cxtStructLimit = 1 << 12;
    private final ObjectArray<String[]> cxtStructs     = new ObjectArray<>(true);
    private final OutputPool<int[]>     cxtStructArea  = new OutputPool<>();

    final List<String>  nameAdded   = new ArrayList<>();
    final List<Integer> nameExpired = new ArrayList<>();

    public OutputSchema(boolean enableCxt) {
        this.enableCxt = enableCxt;
    }

    /**
     * Reset for new round's output.
     */
    public void clear() {
        if (enableCxt) {
            // try release struct
            if (cxtStructArea.size() > cxtStructLimit) {
                long[] releasedItem = cxtStructArea.release(cxtStructLimit / 10);
                for (long item : releasedItem) {
                    unreference(cxtStructArea.get((int) item));
                }
            }
            // try release name
            while (names.size() > cxtNameLimit) {
                long[] releasedItems = cxtStructArea.release(cxtStructLimit / 10);
                for (long item : releasedItems) {
                    unreference(cxtStructArea.get((int) item));
                }
            }
            this.nameExpired.clear();
            this.nameAdded.clear();
            this.cxtStructArea.resetContext();
        }
        this.tmpNames.clear();
        this.tmpStructs.clear();
        this.tmpStructArea.clear();
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
        cxtStructArea.add(nameIds);
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
            id = nameIdPool.acquire();
            if (id >= nameRefCounts.length) {
                int[] refCounts = new int[this.nameRefCounts.length];
                System.arraycopy(this.nameRefCounts, 0, refCounts, 0, this.nameRefCounts.length);
                this.nameRefCounts = refCounts;
            }
            nameRefCounts[id] = 0;
            names.add(id, name);
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
            nameExpired.add(nameId);
        }
    }

}
