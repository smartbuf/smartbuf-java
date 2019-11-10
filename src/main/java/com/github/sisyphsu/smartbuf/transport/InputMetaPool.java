package com.github.sisyphsu.smartbuf.transport;

import com.github.sisyphsu.smartbuf.exception.InvalidStructException;
import com.github.sisyphsu.smartbuf.exception.UnexpectedReadException;

import java.io.IOException;

import static com.github.sisyphsu.smartbuf.transport.Const.*;

/**
 * @author sulin
 * @since 2019-11-04 17:29:53
 */
public final class InputMetaPool {

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
            byte flag = (byte) (head & 0b0000_1110);
            switch (flag) {
                case FLAG_META_NAME_TMP:
                    for (int i = 0; i < size; i++) {
                        tmpNames.add(buf.readString());
                    }
                    break;
                case FLAG_META_NAME_ADDED:
                    for (int i = 0; i < size; i++) {
                        int offset = cxtNameId.acquire();
                        cxtNames.put(offset, buf.readString());
                    }
                    break;
                case FLAG_META_NAME_EXPIRED:
                    for (int i = 0; i < size; i++) {
                        int id = (int) buf.readVarUint();
                        cxtNameId.release(id);
                        cxtNames.put(id, null);
                    }
                    break;
                case FLAG_META_STRUCT_TMP:
                    for (int i = 0; i < size; i++) {
                        int nameCount = (int) buf.readVarUint();
                        String[] names = new String[nameCount];
                        for (int j = 0; j < nameCount; j++) {
                            names[j] = tmpNames.get((int) buf.readVarUint());
                        }
                        tmpStructs.add(names);
                    }
                    break;
                case FLAG_META_STRUCT_ADDED:
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
                case FLAG_META_STRUCT_EXPIRED:
                    for (int i = 0; i < size; i++) {
                        int structId = (int) buf.readVarUint();
                        cxtStructID.release(structId);
                        cxtStructs.put(structId, null);
                    }
                    break;
                default:
                    throw new UnexpectedReadException("invalid meta flag: " + flag);
            }
        }
    }

    /**
     * Find the specified struct's fields by its unique id
     *
     * @param id Struct's unique ID
     * @return Struct's fields
     */
    public String[] findStructByID(int id) throws InvalidStructException {
        if (id == 0) {
            return EMPTY_STRUCT;
        }
        int index = (id >>> 1) - 1;
        if (index < 0) {
            throw new InvalidStructException("invalid struct id: " + id);
        }
        if ((id & 1) == 0) {
            if (index >= tmpStructs.size()) {
                throw new InvalidStructException("invalid temporary struct id: " + id);
            }
            return tmpStructs.get(index);
        }
        if (index >= cxtStructs.cap()) {
            throw new InvalidStructException("invalid context struct id: " + id);
        }
        String[] struct = cxtStructs.get(index);
        if (struct == null) {
            throw new InvalidStructException("invalid context struct id: " + id);
        }
        return struct;
    }

    public void reset() {
        this.tmpNames.clear();
        this.tmpStructs.clear();
    }

}
