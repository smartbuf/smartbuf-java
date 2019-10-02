package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.*;
import com.github.sisyphsu.nakedata.context.model.FrameMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * 需要为ObjectNode提供一种非常便利的struct-fields-id映射关系。
 * <p>
 * 搞一个全局的StructMap，为每个Struct分配一个唯一ID，这样做可以节省1ns的hashcode耗时
 *
 * @author sulin
 * @since 2019-09-26 21:32:54
 */
public final class OutputSchema {

    private final boolean   enableCxt;
    private final FrameMeta meta;

    private final Map<String[], Integer> structMap = new HashMap<>();

    private final int           cxtNameLimit    = 1 << 16;
    private final IDAllocator   nameIdAllocator = new IDAllocator();
    private       int[]         nameRefCounts   = new int[4];
    private final Array<String> names           = new Array<>(true);

    private final int                 cxtStructLimit = 1 << 12;
    private final Array<String[]>     cxtStructs     = new Array<>(true);
    private final RecycleArray<int[]> cxtStructArea  = new RecycleArray<>();

    private final int                  cxtSymbolLimit = 1 << 16;
    private final RecycleArray<String> cxtSymbolArea  = new RecycleArray<>();

    /**
     * Initialize
     */
    public OutputSchema(FrameMeta meta) {
        this.meta = meta;
        this.enableCxt = meta.isEnableCxt();
    }

    /**
     * Reset for new round's output.
     */
    public void tryRelease() {
        if (!enableCxt) {
            return;
        }
        // try release struct
        if (cxtStructArea.size() > cxtStructLimit) {
            long[] releasedItem = cxtStructArea.release(cxtStructLimit / 10);
            for (long l : releasedItem) {
                meta.getCxtStructExpired().add((int) l);
            }
            for (long item : releasedItem) {
                unreference(cxtStructArea.get((int) item));
            }
        }
        // try release name
        while (names.size() > cxtNameLimit) {
            long[] releasedItems = cxtStructArea.release(cxtStructLimit / 10);
            for (long l : releasedItems) {
                meta.getCxtStructExpired().add((int) l);
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
        if (structMap.containsKey(fields)) {
            return;
        }
        int[] nameIds = new int[fields.length];
        for (int i = 0; i < fields.length; i++) {
            String name = fields[i];
            meta.getTmpNames().add(name);
            nameIds[i] = meta.getTmpNames().offset(name);
        }
        meta.getTmpStructs().add(nameIds);
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
            meta.getCxtStructAdded().add(nameIds);
        }
    }

    /**
     * Register symbol for context sharing, could repeat
     */
    public void addSymbol(String symbol) {
        if (meta.isEnableCxt()) {
            if (cxtSymbolArea.add(symbol)) {
                meta.getCxtSymbolAdded().add(symbol);
            }
        } else {
            meta.getTmpStringArea().add(symbol);
        }
    }

    /**
     * Find the specified float's id
     */
    public int findFloatID(float val) {
        return 0;
    }

    /**
     * Find the specified double's id
     */
    public int findDoubleID(double val) {
        return 0;
    }

    /**
     * Find the specified varint's id
     */
    public int findVarintID(long val) {
        return 0;
    }

    /**
     * Find the specified string's id
     */
    public int findStringID(String str) {
        return 0;
    }

    /**
     * Find the specified symbol's id
     */
    public int findSymbolID(String symbol) {
        return 0;
    }

    /**
     * Find the specified temparary struct's id
     */
    public int findTmpStructID(String[] fields) {
        return structMap.get(fields);
    }

    /**
     * Find the specified context struct's id
     */
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
            meta.getCxtNameAdded().add(name); // record nameAdded for context-sync.
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
            meta.getCxtNameExpired().add(nameId);
        }
    }

}
