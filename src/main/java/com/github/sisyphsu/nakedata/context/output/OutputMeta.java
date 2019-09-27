package com.github.sisyphsu.nakedata.context.output;

/**
 * 需要为ObjectNode提供一种非常便利的struct-fields-id映射关系。
 * <p>
 * 搞一个全局的StructMap，为每个Struct分配一个唯一ID，这样做可以节省1ns的hashcode耗时
 *
 * @author sulin
 * @since 2019-09-26 21:32:54
 */
public final class OutputMeta {

    private final int cxtNameLimit   = 1 << 16;
    private final int cxtStructLimit = 1 << 12;

    private final OutputContextName     cxtNameArea   = new OutputContextName();
    private final OutputArray<String[]> cxtStructs    = new OutputArray<>();
    private final OutputArea<int[]>     cxtStructArea = new OutputArea<>();

    private final OutputArray<String>   tmpNameArea   = new OutputArray<>();
    private final OutputArray<String[]> tmpStructs    = new OutputArray<>();
    private final OutputArray<int[]>    tmpStructArea = new OutputArray<>();

    /**
     * Reset for new round's output.
     */
    public void preRelease() {
        cxtNameArea.resetContext();
        cxtStructArea.resetContext();
        // try release struct
        if (cxtStructArea.size() > cxtStructLimit) {
            long[] releasedItem = cxtStructArea.release(cxtStructLimit / 10);
            for (long item : releasedItem) {
                cxtNameArea.unreference(cxtStructArea.get((int) item));
            }
        }
        // try release name
        while (cxtNameArea.size() > cxtNameLimit) {
            long[] releasedItems = cxtStructArea.release(cxtStructLimit / 10);
            for (long item : releasedItems) {
                cxtNameArea.unreference(cxtStructArea.get((int) item));
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
            nameIds[i] = cxtNameArea.registerName(fields[i]);
        }
        cxtStructArea.add(nameIds);
    }

    public int findTmpStructID(String[] fields) {
        return tmpStructs.getID(fields);
    }

    public int findCxtStructID(String[] fields) {
        return cxtStructs.getID(fields);
    }

}
