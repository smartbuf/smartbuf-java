package com.github.sisyphsu.canoe.transport;

import java.io.IOException;

import static com.github.sisyphsu.canoe.transport.Const.*;

/**
 * @author sulin
 * @since 2019-11-04 17:29:53
 */
public class InputMetaPool {

    private static final String[] EMPTY_STRUCT = new String[0];

    private final IDAllocator   cxtNameId = new IDAllocator();
    private final Array<String> cxtNames  = new Array<>();
    private final Array<String> tmpNames  = new Array<>();

    private final IDAllocator     cxtStructID = new IDAllocator();
    private final Array<String[]> cxtStructs  = new Array<>();
    private final Array<String[]> tmpStructs  = new Array<>();

    /**
     * Read meta info from the specified buffer.
     *
     * @param buf The specified input buffer
     */
    public void read(InputBuffer buf) throws IOException {
        boolean hasMore = true;
        while (hasMore) {
            long head = buf.readVarUint();
            int size = (int) (head >>> 4);
            hasMore = (head & 0b0000_0001) == 1;
            byte flag = (byte) ((head >>> 1) & 0b0000_0111);
            switch (flag) {
                case META_NAME_TMP:
                    for (int i = 0; i < size; i++) {
                        tmpNames.add(buf.readString());
                    }
                    break;
                case META_NAME_ADDED:
                    for (int i = 0; i < size; i++) {
                        int offset = cxtNameId.acquire();
                        cxtNames.put(offset, buf.readString());
                    }
                    break;
                case META_NAME_EXPIRED:
                    for (int i = 0; i < size; i++) {
                        int id = (int) buf.readVarUint();
                        cxtNameId.release(id);
                        cxtNames.put(id, null);
                    }
                    break;
                case META_STRUCT_TMP:
                    for (int i = 0; i < size; i++) {
                        int nameCount = (int) buf.readVarUint();
                        String[] names = new String[nameCount];
                        for (int j = 0; j < nameCount; j++) {
                            names[j] = tmpNames.get((int) buf.readVarUint());
                        }
                        tmpStructs.add(names);
                    }
                    break;
                case META_STRUCT_ADDED:
                    for (int i = 0; i < size; i++) {
                        int nameCount = (int) buf.readVarUint();
                        String[] names = new String[nameCount];
                        for (int j = 0; j < nameCount; j++) {
                            names[j] = cxtNames.get((int) buf.readVarUint());
                        }
                        int structId = cxtStructID.acquire();
                        cxtStructs.put(structId, names);
                    }
                    break;
                case META_STRUCT_EXPIRED:
                    for (int i = 0; i < size; i++) {
                        int structId = (int) buf.readVarUint();
                        cxtStructID.release(structId);
                        cxtStructs.put(structId, null);
                    }
                    break;
                default:
                    throw new RuntimeException("invalid flag: " + flag);
            }
        }
    }

    /**
     * Find the specified struct's fields by its unique id
     *
     * @param id Struct's unique ID
     * @return Struct's fields
     */
    public String[] findStructByID(int id) {
        if (id == 0) {
            return EMPTY_STRUCT;
        }
        int index = (id >>> 1) - 1;
        if (index < 0) {
            throw new IllegalArgumentException("invalid temporary struct id: " + id);
        }
        if ((id & 1) == 0) {
            if (index >= tmpStructs.size()) {
                throw new IllegalArgumentException("invalid temporary struct id: " + id);
            }
            return tmpStructs.get(index);
        }
        if (index >= cxtStructs.cap()) {
            throw new IllegalArgumentException("invalid context struct id: " + id);
        }
        String[] struct = cxtStructs.get(id);
        if (struct == null) {
            throw new IllegalArgumentException("invalid context struct id: " + id);
        }
        return struct;
    }

}
