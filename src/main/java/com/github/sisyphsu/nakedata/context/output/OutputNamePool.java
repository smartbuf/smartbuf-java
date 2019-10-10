package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.common.IDAllocator;

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

    private final Array<String> tmpNames     = new Array<>();
    private final Array<String> cxtNameAdded = new Array<>();

    private final IDAllocator   cxtIdAlloc = new IDAllocator();
    private final Array<String> cxtNames   = new Array<>();

    private final Map<String, Name> index = new HashMap<>();

    /**
     * Register the specified names into this pool, could repeat.
     *
     * @param temporary It's temporary or not
     * @param names     The names to register
     */
    public void register(boolean temporary, String... names) {
        for (String name : names) {
            Name meta = index.get(name);
            if (meta != null) {
                if (temporary) {
                    continue;
                }
                if (!meta.temporary) {
                    meta.refCount++;
                    continue;
                }
                tmpNames.size--;
                if (meta.offset < tmpNames.size) {
                    String lastTmp = tmpNames.get(tmpNames.size);
                    tmpNames.put(meta.offset, lastTmp);
                    index.get(lastTmp).offset = meta.offset;
                }
                this.index.remove(name);
            }
            if (temporary) {
                meta = new Name(true, tmpNames.size, name);
                this.tmpNames.add(name);
            } else {
                int offset = cxtIdAlloc.acquire();
                meta = new Name(false, offset, name);
                this.cxtNames.put(offset, name);
                this.cxtNameAdded.add(name); // record for outter using
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
            Name meta = index.get(name);
            if (meta == null || meta.temporary) {
                continue;
            }
            if (--meta.refCount <= 0) {
                cxtNames.put(meta.offset, null);
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
        Name meta = index.get(name);
        if (meta == null) {
            throw new IllegalArgumentException("not exists: " + name);
        }
        if (meta.temporary) {
            return meta.offset;
        }
        return tmpNames.size + meta.offset;
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
        if (id < tmpNames.size) {
            return tmpNames.get(id);
        }
        id -= tmpNames.size;
        if (id > cxtIdAlloc.count()) {
            throw new IllegalArgumentException("invalid id: " + id);
        }
        String result = cxtNames.get(id);
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
        for (int i = 0; i < tmpNames.size; i++) {
            index.remove(tmpNames.get(i));
        }
        this.tmpNames.size = 0;
        this.cxtNameAdded.size = 0;
    }

    // field-name's metadata
    private final static class Name {
        String  name;
        boolean temporary;
        int     offset;
        int     refCount;

        public Name(boolean temporary, int offset, String name) {
            this.temporary = temporary;
            this.offset = offset;
            this.refCount = 1;
            this.name = name;
        }
    }

}
