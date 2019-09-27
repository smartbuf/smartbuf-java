package com.github.sisyphsu.nakedata.context.output;

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
public final class OutputMeta {

    private final OutputArray<String>   tmpNameArea   = new OutputArray<>();
    private final OutputArray<String[]> tmpStructs    = new OutputArray<>();
    private final OutputArray<int[]>    tmpStructArea = new OutputArray<>();

    private final int                 cxtNameLimit  = 1 << 16;
    private final IDPool              nameIdPool    = new IDPool();
    private       int[]               nameRefCounts = new int[4];
    private final OutputArray<String> names         = new OutputArray<>();

    private final int                   cxtStructLimit = 1 << 12;
    private final OutputArray<String[]> cxtStructs     = new OutputArray<>();
    private final OutputArea<int[]>     cxtStructArea  = new OutputArea<>();

    final List<String>  nameAdded   = new ArrayList<>();
    final List<Integer> nameExpired = new ArrayList<>();

    /**
     * Reset for new round's output.
     */
    public void preRelease() {
        this.nameExpired.clear();
        this.nameAdded.clear();
        cxtStructArea.resetContext();
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
            tmpNameArea.add(name);
            nameIds[i] = tmpNameArea.getID(name);
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
        return tmpStructs.getID(fields);
    }

    public int findCxtStructID(String[] fields) {
        return cxtStructs.getID(fields);
    }

    private int registerName(String name) {
        Integer id = this.names.getID(name);
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
