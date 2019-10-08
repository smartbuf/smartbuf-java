package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.common.IDAllocator;
import com.github.sisyphsu.nakedata.utils.ArrayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * NamePool represents an area holds field-names for sharing, which support temporary and context using.
 * <p>
 * It will allocate an unique id for every name.
 *
 * @author sulin
 * @since 2019-10-07 21:28:57
 */
public final class OutputNamePool {

    private int      tmpNameCount      = 0;
    private String[] tmpNames          = new String[4];
    private int      cxtNameAddedCount = 0;
    private String[] cxtNameAdded      = new String[4];

    private       String[]    cxtNames   = new String[4];
    private final IDAllocator cxtIdAlloc = new IDAllocator();

    private final Map<String, NameMeta> index = new HashMap<>();

    /**
     * Register the specified names into this pool, could repeat.
     *
     * @param temporary It's temporary or not
     * @param names     The names to register
     */
    public void register(boolean temporary, String... names) {
        for (String name : names) {
            NameMeta meta = index.get(name);
            if (meta != null) {
                if (temporary) {
                    continue;
                }
                if (!meta.temporary) {
                    meta.refCount++;
                    continue;
                }
                tmpNameCount--;
                if (meta.offset < tmpNameCount) {
                    String lastTmp = tmpNames[tmpNameCount];
                    tmpNames[meta.offset] = lastTmp;
                    index.get(lastTmp).offset = meta.offset;
                }
                this.index.remove(name);
            }
            if (temporary) {
                meta = new NameMeta(true, tmpNameCount);
                this.tmpNames = ArrayUtils.put(tmpNames, tmpNameCount++, name);
            } else {
                int offset = cxtIdAlloc.acquire();
                meta = new NameMeta(false, offset);
                this.cxtNames = ArrayUtils.put(cxtNames, offset, name);
                this.cxtNameAdded = ArrayUtils.put(cxtNameAdded, cxtNameAddedCount++, name); // record for outter using
            }
            index.put(name, meta);
        }
    }

    /**
     * Unregister the specified names from this pool
     *
     * @param names The names to unregister
     */
    public void unregister(String... names) {
        for (String name : names) {
            NameMeta meta = index.get(name);
            if (meta == null || meta.temporary) {
                continue;
            }
            if (--meta.refCount <= 0) {
                cxtNames[meta.offset] = null;
                cxtIdAlloc.release(meta.offset);
                index.remove(name);
            }
            // don't need to sync peer
        }
    }

    /**
     * Fetch unique id of the specified name by temporary and context names
     *
     * @param name The specified name to fetch id
     * @return unique id
     */
    public int findNameID(String name) {
        NameMeta meta = index.get(name);
        if (meta == null) {
            throw new IllegalArgumentException("not exists: " + name);
        }
        if (meta.temporary) {
            return meta.offset;
        }
        return tmpNameCount + meta.offset;
    }

    /**
     * Fetch the specified name by its unique id
     *
     * @param id The unique id
     * @return name
     */
    public String findNameByID(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("negative id: " + id);
        }
        if (id < tmpNameCount) {
            return tmpNames[id];
        }
        id -= tmpNameCount;
        if (id > cxtIdAlloc.count()) {
            throw new IllegalArgumentException("invalid id: " + id);
        }
        String result = cxtNames[id];
        if (result == null) {
            throw new IllegalArgumentException("invalid id: " + id);
        }
        return result;
    }

    /**
     * Fetch total number of name in temporary and context area.
     *
     * @return Total number
     */
    public int size() {
        return index.size();
    }

    /**
     * Reset this pool, clear all temporary data, and keep context status.
     */
    public void reset() {
        for (int i = 0; i < tmpNameCount; i++) {
            index.remove(tmpNames[i]);
        }
        this.tmpNameCount = 0;
        this.cxtNameAddedCount = 0;
    }

    // field-name's metadata
    private final static class NameMeta {

        boolean temporary;
        int     offset;
        int     refCount;

        public NameMeta(boolean temporary, int offset) {
            this.temporary = temporary;
            this.offset = offset;
            this.refCount = 1;
        }
    }

}
