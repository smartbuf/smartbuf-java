package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.Array;
import com.github.sisyphsu.nakedata.common.RefExpireArray;
import com.github.sisyphsu.nakedata.common.TimeExpireArray;
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

    private final int                    cxtNameLimit = 1 << 16;
    private final RefExpireArray<String> cxtNameArea  = new RefExpireArray<>();

    private final int                    cxtStructLimit = 1 << 12;
    private final Array<String[]>        cxtStructs     = new Array<>(true);
    private final TimeExpireArray<int[]> cxtStructArea  = new TimeExpireArray<>();

    private final int                     cxtSymbolLimit = 1 << 16;
    private final TimeExpireArray<String> cxtSymbolArea  = new TimeExpireArray<>();

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
                cxtNameArea.unreference(cxtStructArea.get((int) item));
            }
        }
        // try release name
        while (cxtNameArea.size() > cxtNameLimit) {
            long[] releasedItems = cxtStructArea.release(cxtStructLimit / 10);
            for (long l : releasedItems) {
                meta.getCxtStructExpired().add((int) l);
            }
            for (long item : releasedItems) {
                cxtNameArea.unreference(cxtStructArea.get((int) item));
            }
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
     * Register struct for sharing, coud repeat
     */
    public void addStruct(String[] fields) {
        if (cxtStructs.contains(fields)) {
            return;
        }
        int[] nameIds = new int[fields.length];
        if (meta.isEnableCxt()) {
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i];
                if (cxtNameArea.add(fieldName)) {
                    meta.getCxtNameAdded().add(fieldName);
                }
                nameIds[i] = cxtNameArea.offset(fieldName);
            }
            if (cxtStructArea.add(nameIds)) {
                meta.getCxtStructAdded().add(nameIds);
            }
        } else {
            for (int i = 0; i < fields.length; i++) {
                String fieldName = fields[i];
                meta.getTmpNames().add(fieldName);
                nameIds[i] = meta.getTmpNames().offset(fieldName);
            }
            meta.getTmpStructs().add(nameIds);
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

}
