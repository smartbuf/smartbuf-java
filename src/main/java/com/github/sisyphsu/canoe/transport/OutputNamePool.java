package com.github.sisyphsu.canoe.transport;

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

    final Array<Name>    tmpNames = new Array<>();
    final Array<Name>    cxtNameAdded;
    final Array<Integer> cxtNameExpired;

    private final IDAllocator cxtIdAlloc = new IDAllocator();
    private final Array<Name> cxtNames   = new Array<>();

    private final Map<String, Name> index = new HashMap<>();

    public OutputNamePool(Schema schema) {
        this.cxtNameAdded = new Array<>();
        this.cxtNameExpired = schema.cxtNameExpired;
    }

    /**
     * Register the specified names into this pool, could repeat.
     *
     * @param temporary It's temporary or not
     * @param names     The names to register
     */
    public void register(boolean temporary, String... names) {
        for (String nameStr : names) {
            Name name = index.get(nameStr);
            if (name != null) {
                if (temporary) {
                    continue;
                }
                if (!name.temporary) {
                    name.refCount++;
                    continue;
                }
                Name lastName = tmpNames.popLast();
                if (lastName != name) {
                    lastName.offset = name.offset;
                    tmpNames.put(name.offset, lastName);
                }
                this.index.remove(nameStr);
            }
            if (temporary) {
                name = new Name(true, tmpNames.size(), nameStr);
                this.tmpNames.add(name);
            } else {
                int offset = cxtIdAlloc.acquire();
                name = new Name(false, offset, nameStr);
                this.cxtNames.put(offset, name);
                this.cxtNameAdded.add(name); // record for outter using
            }
            index.put(nameStr, name);
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
            if (meta == null || meta.temporary || --meta.refCount > 0) {
                continue;
            }
            cxtNames.put(meta.offset, null);
            cxtIdAlloc.release(meta.offset);
            cxtNameExpired.add(meta.offset);
            index.remove(name);
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
        return tmpNames.size() + meta.offset;
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
        for (int i = 0, len = tmpNames.size(); i < len; i++) {
            index.remove(tmpNames.get(i).name);
        }
        this.tmpNames.clear();
        this.cxtNameAdded.clear();
    }

    /**
     * field-name's metadata
     */
    final static class Name {
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
