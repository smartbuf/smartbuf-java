package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.utils.IDPool;

import java.util.ArrayList;
import java.util.List;

/**
 * 1.
 *
 * @author sulin
 * @since 2019-09-26 15:03:15
 */
public final class OutputContextName {

    private final IDPool             idPool    = new IDPool();
    private final OutputList<String> names     = new OutputList<>();
    private       int[]              refCounts = new int[4];

    final List<String>  nameAdded   = new ArrayList<>();
    final List<Integer> nameExpired = new ArrayList<>();

    /**
     * Register an name into the context for sharing, and return its unique id.
     *
     * @param name The specified name string, could repeat.
     * @return name's unique id
     */
    public int registerName(String name) {
        Integer id = this.names.getID(name);
        if (id == null) {
            id = idPool.acquire();
            if (id >= refCounts.length) {
                int[] refCounts = new int[this.refCounts.length];
                System.arraycopy(this.refCounts, 0, refCounts, 0, this.refCounts.length);
                this.refCounts = refCounts;
            }
            refCounts[id] = 0;
            names.add(id, name);
            nameAdded.add(name); // record nameAdded for context-sync.
        }
        return id;
    }

    /**
     * Inform this name-area that some names were referenced by new struct.
     *
     * @param nameIds IDs of names.
     */
    public void reference(int[] nameIds) {
        for (int nameId : nameIds) {
            refCounts[nameId]++;
        }
    }

    /**
     * Inform the name area that some name are unreferenced because a struct was dead.
     *
     * @param nameIds ID of names.
     */
    public void unreference(int[] nameIds) {
        for (int nameId : nameIds) {
            int refCount = this.refCounts[nameId];
            if (refCount > 1) {
                refCounts[nameId] = refCount - 1;
            }
            // nameId should be released
            names.remove(nameId);
            nameExpired.add(nameId);
        }
    }

    public int size() {
        return names.size();
    }

    public void resetContext() {
        this.nameExpired.clear();
        this.nameAdded.clear();
    }

}
