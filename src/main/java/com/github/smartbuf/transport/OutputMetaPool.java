package com.github.smartbuf.transport;

import com.github.smartbuf.utils.TimeUtils;

import java.io.IOException;
import java.util.*;

/**
 * StructPool represents an area holds struct for sharing, which support temporary and context using.
 * <p>
 * It will allocate unique ID for every struct by its fields.
 *
 * @author sulin
 * @since 2019-10-07 21:29:36
 */
public final class OutputMetaPool {

    private static final byte HAS_NAME_TMP        = 1;
    private static final byte HAS_NAME_ADDED      = 1 << 1;
    private static final byte HAS_NAME_EXPIRED    = 1 << 2;
    private static final byte HAS_STRUCT_TMP      = 1 << 3;
    private static final byte HAS_STRUCT_ADDED    = 1 << 4;
    private static final byte HAS_STRUCT_EXPIRED  = 1 << 5;
    private static final byte HAS_STRUCT_REFERRED = 1 << 6;

    private static final byte NEED_SEQ = HAS_NAME_ADDED | HAS_NAME_EXPIRED | HAS_STRUCT_ADDED | HAS_STRUCT_EXPIRED;

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

    final IDAllocator        cxtStructIdAlloc  = new IDAllocator();
    final Array<Struct>      cxtStructs        = new Array<>();
    final Array<Struct>      cxtStructAdded    = new Array<>();
    final Array<Integer>     cxtStructExpired  = new Array<>();
    final Array<Struct>      cxtStructReferred = new Array<>();
    final Map<Names, Struct> cxtStructIndex    = new HashMap<>();

    private byte status;

    /**
     * Initialize StructPool with the specified limit of context-struct
     *
     * @param limit Max number of context-struct
     */
    public OutputMetaPool(int limit) {
        this.cxtStructLimit = limit;
    }

    /**
     * Register an struct for temporary using
     *
     * @param names Names which represents an temporary struct
     * @return Struct's ID
     */
    public int registerTmpStruct(String... names) {
        if (names == null) {
            throw new NullPointerException("names is null");
        }
        if (names.length == 0) {
            return 0;
        }
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
            struct.id = (struct.index + 1) << 1; // identify temporary struct by suffixed 0
            tmpStructIndex.put(struct, struct);
        }
        return struct.id;
    }

    /**
     * Register the specified struct into pool by its field-names.
     *
     * @param names FieldNames which represents an struct
     * @return Struct ID
     */
    public int registerCxtStruct(String... names) {
        if (names == null) {
            throw new NullPointerException("names is null");
        }
        if (names.length == 0) {
            return 0;
        }
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
                    cxtNames.put(index, name);
                    cxtNameAdded.add(name); // record for outter using
                    cxtNameIndex.put(str, name);
                }
                nameIds[off++] = name.index;
                name.refCount++;
            }
            struct = new Struct(names, nameIds);
            struct.index = cxtStructIdAlloc.acquire();
            struct.id = ((struct.index + 1) << 1) | 1; // identify context struct by suffixed 1
            this.cxtStructs.put(struct.index, struct);
            this.cxtStructAdded.add(struct);
            this.cxtStructIndex.put(struct, struct);
        }
        struct.lastTime = (int) TimeUtils.fastUpTime();
        if (!struct.refered) {
            struct.refered = true;
            this.cxtStructReferred.add(struct);
        }
        return struct.id;
    }

    boolean needOutput() {
        byte status = 0;
        if (tmpNames.size() > 0) status |= HAS_NAME_TMP;
        if (cxtNameAdded.size() > 0) status |= HAS_NAME_ADDED;
        if (cxtNameExpired.size() > 0) status |= HAS_NAME_EXPIRED;
        if (tmpStructs.size() > 0) status |= HAS_STRUCT_TMP;
        if (cxtStructAdded.size() > 0) status |= HAS_STRUCT_ADDED;
        if (cxtStructExpired.size() > 0) status |= HAS_STRUCT_EXPIRED;
        if (cxtStructReferred.size() > 0) status |= HAS_STRUCT_REFERRED;
        this.status = status;
        return status > 0;
    }

    boolean needSequence() {
        return (status & NEED_SEQ) > 0;
    }

    /**
     * Write this MetaPool into the specified output buffer.
     */
    void write(OutputBuffer buf) throws IOException {
        int len;
        byte status = this.status;
        if ((status & HAS_NAME_TMP) > 0) {
            status ^= HAS_NAME_TMP;
            len = tmpNames.size();
            buf.writeVarUint((len << 4) | Const.FLAG_META_NAME_TMP | 1); // must have structTmp
            for (int i = 0; i < len; i++) {
                buf.writeString(tmpNames.get(i));
            }
        }
        if ((status & HAS_NAME_EXPIRED) > 0) {
            status ^= HAS_NAME_EXPIRED;
            len = cxtNameExpired.size();
            buf.writeVarUint((len << 4) | Const.FLAG_META_NAME_EXPIRED | 1); // must have structExpired
            for (int i = 0; i < len; i++) {
                buf.writeVarUint(cxtNameExpired.get(i));
            }
        }
        if ((status & HAS_NAME_ADDED) > 0) {
            status ^= HAS_NAME_ADDED;
            len = cxtNameAdded.size();
            buf.writeVarUint((len << 4) | Const.FLAG_META_NAME_ADDED | 1); // must have structAdded
            for (int i = 0; i < len; i++) {
                buf.writeString(cxtNameAdded.get(i).name);
            }
        }
        if ((status & HAS_STRUCT_TMP) > 0) {
            status ^= HAS_STRUCT_TMP;
            len = tmpStructs.size();
            buf.writeVarUint((len << 4) | Const.FLAG_META_STRUCT_TMP | (status == 0 ? 0 : 1));
            for (int i = 0; i < len; i++) {
                OutputMetaPool.Struct struct = tmpStructs.get(i);
                buf.writeVarUint(struct.nameIds.length);
                for (int nameId : struct.nameIds) {
                    buf.writeVarUint(nameId);
                }
            }
        }
        if ((status & HAS_STRUCT_EXPIRED) > 0) {
            len = cxtStructAdded.size();
            status ^= HAS_STRUCT_EXPIRED;
            buf.writeVarUint((len << 4) | Const.FLAG_META_STRUCT_EXPIRED | (status == 0 ? 0 : 1));
            for (int i = 0; i < len; i++) {
                buf.writeVarUint(cxtStructExpired.get(i));
            }
        }
        if ((status & HAS_STRUCT_ADDED) > 0) {
            len = cxtStructAdded.size();
            status ^= HAS_STRUCT_ADDED;
            buf.writeVarUint((len << 4) | Const.FLAG_META_STRUCT_ADDED | 1); // must has HAS_STRUCT_REFERRED suffixed
            for (int i = 0; i < len; i++) {
                int[] nameIds = cxtStructAdded.get(i).nameIds;
                buf.writeVarUint(nameIds.length);
                for (int nameId : nameIds) {
                    buf.writeVarUint(nameId);
                }
            }
        }
        if ((status & HAS_STRUCT_REFERRED) > 0) {
            len = cxtStructReferred.size();
            buf.writeVarUint((len << 4) | Const.FLAG_META_STRUCT_REFERRED);
            for (int i = 0; i < len; i++) {
                Struct struct = cxtStructReferred.get(i);
                buf.writeVarUint(struct.index);
                buf.writeVarUint(struct.names.length);
            }
        }
    }

    /**
     * Reset this struct pool, and execute struct-expire automatically
     */
    void reset() {
        this.tmpNames.clear();
        this.tmpNameIndex.clear();
        this.cxtNameAdded.clear();
        this.cxtNameExpired.clear();

        this.tmpStructs.clear();
        this.tmpStructIndex.clear();
        this.cxtStructAdded.clear();
        this.cxtStructExpired.clear();
        this.cxtStructReferred.clear();

        for (Struct struct : cxtStructIndex.values()) {
            struct.refered = false;
        }

        // execute automatically expire for context-struct
        int expireCount = cxtStructIndex.size() - cxtStructLimit;
        if (expireCount <= 0) {
            return;
        }
        List<Struct> structs = new ArrayList<>(cxtStructIndex.values());
        structs.sort(Comparator.comparingInt(s -> s.lastTime));
        for (int i = 0, len = Math.min(expireCount, structs.size()); i < len; i++) {
            Struct expiredStruct = structs.get(i);
            cxtStructIndex.remove(expiredStruct);
            cxtStructIdAlloc.release(expiredStruct.index);
            cxtStructExpired.add(expiredStruct.index);
            cxtStructs.put(expiredStruct.index, null);
            // synchronize cxtNames
            for (int nameId : expiredStruct.nameIds) {
                Name meta = cxtNames.get(nameId);
                meta.refCount--;
                if (meta.refCount == 0) {
                    cxtNames.put(meta.index, null);
                    cxtIdAlloc.release(meta.index);
                    cxtNameExpired.add(meta.index);
                    cxtNameIndex.remove(meta.name);
                }
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
            return obj instanceof Names && Arrays.equals(names, ((Names) obj).names);
        }
    }

    /**
     * Struct model for inner usage
     */
    static class Struct extends Names {
        int     id;
        int     index;
        int     lastTime;
        int[]   nameIds;
        boolean refered;

        public Struct(String[] names, int[] nameIds) {
            this.names = names;
            this.nameIds = nameIds;
        }
    }
}
