package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.common.IDPool;
import com.github.sisyphsu.nakedata.common.NameHeap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author sulin
 * @since 2019-04-29 13:39:46
 */
public class ContextNamePool {

    private static final double FACTOR_KEEP = 0.9;

    /**
     * When reach limit, keep some more active names.
     */
    private int keep;
    /**
     * Max count of name cached in Context.
     */
    private int limit;
    /**
     * Provide incremental id acquire and release features.
     */
    private IDPool pool;
    /**
     * Maintain the relationship between id and ContextName.
     */
    private Map<Integer, ContextName> idMap = new HashMap<>();
    /**
     * Maintain the relationship between name and ContextName.
     */
    private Map<String, ContextName> nameMap = new HashMap<>();

    /**
     * Initialize pool
     *
     * @param limit Name's count limit.
     */
    public ContextNamePool(int limit) {
        this.limit = limit;
        this.keep = (int) (limit * FACTOR_KEEP);
        this.pool = new IDPool(limit);
    }

    /**
     * Query real name of the specified id.
     *
     * @param id Name's id
     * @return ContextName
     */
    public ContextName getName(int id) {
        return idMap.get(id);
    }

    /**
     * Add new names into the current pool, if name's count reach limit, auto-release some unactive ids.
     *
     * @param version TODO should use Context's reference, not argument.
     * @param names   Could be new names, or old names.
     */
    public void addNames(ContextVersion version, Set<String> names) {
        // if reach limit, expire some low priority name.
        if (names.size() + nameMap.size() > limit) {
            NameHeap heap = new NameHeap(limit - keep);
            for (ContextName name : nameMap.values()) {
                if (names.contains(name.getName())) {
                    continue;
                }
                heap.filter(name);
            }
            for (ContextName unactiveName : heap.getHeap()) {
                idMap.remove(unactiveName.getId());
                nameMap.remove(unactiveName.getName());
                pool.release(unactiveName.getId());
                version.getNameExpired().add(unactiveName.getId());
            }
        }
        // only add the name which didn't exists.
        for (String name : names) {
            ContextName cxtName = nameMap.get(name);
            if (cxtName == null) {
                cxtName = new ContextName(pool.acquire(), name);
                idMap.put(cxtName.getId(), cxtName);
                nameMap.put(cxtName.getName(), cxtName);
                version.getNameAdded().add(name);
            }
            cxtName.flushRTime();
        }
    }

}
