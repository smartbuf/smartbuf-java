package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.common.Array;
import com.github.sisyphsu.nakedata.context.common.RefExpireArray;
import com.github.sisyphsu.nakedata.context.common.TimeExpireArray;
import com.github.sisyphsu.nakedata.context.model.FrameMeta;
import com.github.sisyphsu.nakedata.node.std.ObjectNode;

/**
 * 需要为ObjectNode提供一种非常便利的struct-fields-id映射关系。
 * <p>
 * 搞一个全局的StructMap，为每个Struct分配一个唯一ID，这样做可以节省1ns的hashcode耗时
 *
 * @author sulin
 * @since 2019-09-26 21:32:54
 */
public final class OutputSchema {

    private final FrameMeta meta;
    private final boolean   enableCxt;

    private final Array<String[]> tmpStructs = new Array<>(true);
    private final Array<String[]> cxtStructs = new Array<>(true);

    private final int                    cxtNameLimit = 1 << 16;
    private final RefExpireArray<String> cxtNameArea  = new RefExpireArray<>();

    private final int                    cxtStructLimit = 1 << 12;
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
    public void preRelease() {
        if (!enableCxt) {
            return;
        }
        if (cxtSymbolArea.size() > cxtSymbolLimit) {
            long[] releasedItems = cxtSymbolArea.release(cxtSymbolLimit / 10);
            for (long releasedItem : releasedItems) {
                meta.getCxtSymbolExpired().add((int) releasedItem);
            }
        }
        while (cxtStructArea.size() > cxtStructLimit || cxtNameArea.size() > cxtNameLimit) {
            long[] releasedItems = cxtStructArea.release(cxtStructLimit / 10);
            for (long item : releasedItems) {
                int structId = (int) item;
                meta.getCxtStructExpired().add(structId);
                cxtNameArea.unreference(cxtStructArea.get(structId)); // don't need send to peer
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
    public void addStruct(ObjectNode.Key key) {
        String[] fields = key.getFields();
        int[] nameIds = new int[fields.length];
        // 不能在此时分配nameId，因为tmpName和cxtName此时是不完整的
        if (enableCxt && key.isStable()) {
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
    public int findStructID(ObjectNode.Key objectKey) {
        return 0;
    }

}
