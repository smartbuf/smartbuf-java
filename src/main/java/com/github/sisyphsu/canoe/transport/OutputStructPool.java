package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.utils.TimeUtils;

import java.util.*;

/**
 * StructPool represents an area holds struct for sharing, which support temporary and context using.
 * <p>
 * It will allocate unique ID for every struct by its fields.
 *
 * @author sulin
 * @since 2019-10-07 21:29:36
 */
public final class OutputStructPool {

    private final int   cxtStructLimit;
    private final Names key = new Names();

    final Array<String>        tmpNames     = new Array<>();
    final Map<String, Integer> tmpNameIndex = new HashMap<>();

    final Array<Struct>      tmpStructs     = new Array<>();
    final Map<Names, Struct> tmpStructIndex = new HashMap<>();

    final IDAllocator       cxtIdAlloc     = new IDAllocator();
    final Array<Name>       cxtNames       = new Array<>();
    final Array<Name>       cxtNameAdded   = new Array<>();
    final Array<Integer>    cxtNameExpired = new Array<>();
    final Map<String, Name> cxtNameIndex   = new HashMap<>();

    final IDAllocator        cxtStructIdAlloc = new IDAllocator();
    final Array<Struct>      cxtStructs       = new Array<>();
    final Array<Struct>      cxtStructAdded   = new Array<>();
    final Array<Struct>      cxtStructExpired = new Array<>();
    final Map<Names, Struct> cxtStructIndex   = new HashMap<>();

    /**
     * Initialize StructPool with the specified limit of context-struct
     *
     * @param limit Max number of context-struct
     */
    public OutputStructPool(int limit) {
        this.cxtStructLimit = limit;
    }

    /**
     * Register an struct for temporary using
     *
     * @param names Names which represents an temporary struct
     * @return Struct's ID
     */
    public int getTmpStructID(String[] names) {
        this.key.names = names;
        Struct struct = tmpStructIndex.get(key);
        if (struct == null) {
            int[] nameIds = new int[names.length];
            int off = 0;
            for (String name : names) {
                Integer nameId = tmpNameIndex.get(name);
                if (nameId == null) {
                    nameId = tmpNames.add(name);
                    tmpNameIndex.put(name, nameId);
                }
                nameIds[off++] = nameId;
            }
            struct = new Struct(names, nameIds);
            struct.index = tmpStructs.add(struct);
            tmpStructIndex.put(key, struct);
        }
        return struct.index;
    }

    /**
     * Register the specified struct into pool by its field-names.
     *
     * @param names FieldNames which represents an struct
     * @return Struct ID
     */
    public int getCxtStructID(String[] names) {
        this.key.names = names;
        Struct struct = cxtStructIndex.get(key);
        if (struct == null) {
            int[] nameIds = new int[names.length];
            int off = 0;
            for (String str : names) {
                Name name = cxtNameIndex.get(str);
                if (name == null) {
                    int index = cxtIdAlloc.acquire();
                    name = new Name(str, index);
                    this.cxtNames.put(index, name);
                    this.cxtNameAdded.add(name); // record for outter using
                }
                nameIds[off++] = name.index;
            }
            struct = new Struct(names, nameIds);
            struct.index = cxtStructIdAlloc.acquire();
            this.cxtStructs.put(struct.index, struct);
            this.cxtStructAdded.add(struct);
            this.cxtStructIndex.put(key, struct);
        }
        struct.lastTime = (int) TimeUtils.fastUpTime();
        return struct.index;
    }

    /**
     * Reset this struct pool, and execute struct-expire automatically
     */
    public void reset() {
        this.tmpNames.clear();
        this.tmpNameIndex.clear();
        this.cxtNameAdded.clear();
        this.cxtNameExpired.clear();

        this.tmpStructs.clear();
        this.tmpStructIndex.clear();
        this.cxtStructAdded.clear();
        this.cxtStructExpired.clear();

        // execute automatically expire for context-struct
        int expireCount = cxtStructIndex.size() - cxtStructLimit;
        if (expireCount <= 0) {
            return;
        }
        List<Struct> structs = new ArrayList<>(cxtStructIndex.values());
        structs.sort(Comparator.comparingInt(s -> s.lastTime));
        for (int i = 0, len = Math.min(expireCount, structs.size()); i < len; i++) {
            Struct expiredStruct = structs.get(i);
            cxtStructExpired.add(expiredStruct);
            cxtStructIndex.remove(expiredStruct);
            cxtStructIdAlloc.release(expiredStruct.index);
            cxtStructs.put(expiredStruct.index, null);
            // synchronize cxtNames
            for (int nameId : expiredStruct.nameIds) {
                Name meta = cxtNames.get(nameId);
                cxtNames.put(meta.index, null);
                cxtIdAlloc.release(meta.index);
                cxtNameExpired.add(meta.index);
                cxtNameIndex.remove(meta.name);
            }
        }
    }

    /**
     * field-name's metadata
     */
    static class Name {
        String name;
        int    index;
        int    refCount;

        public Name(String name, int index) {
            this.name = name;
            this.index = index;
            this.refCount = 1;
        }
    }

    /**
     * used for String[] mapping
     */
    static class Names {
        String[] names;

        @Override
        public int hashCode() {
            return Arrays.hashCode(names);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Struct && Arrays.equals(names, ((Struct) obj).names);
        }
    }

    /**
     * Struct model for inner usage
     */
    static class Struct extends Names {
        int   index;
        int   lastTime;
        int[] nameIds;

        public Struct(String[] names, int[] nameIds) {
            this.names = names;
            this.nameIds = nameIds;
        }
    }
}
