package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.IDPool;
import com.github.sisyphsu.nakedata.common.NameHeap;
import com.github.sisyphsu.nakedata.context.ContextName;
import com.github.sisyphsu.nakedata.context.ContextVersion;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author sulin
 * @since 2019-04-29 13:39:46
 */
public class OutputNamePool {

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
     * Maintain the relationship between name and ContextName.
     */
    private Map<String, ContextName> nameMap = new HashMap<>();

    /**
     * Initialize pool
     *
     * @param limit Name's count limit.
     */
    public OutputNamePool(int limit) {
        this.limit = limit;
        this.keep = (int) (limit * FACTOR_KEEP);
        this.pool = new IDPool(limit);
    }

    /**
     * Get the unique id of the specified name.
     *
     * @param name name's value
     * @return unique id
     */
    public int getNameID(String name) {
        ContextName cxtName = nameMap.get(name);
        return cxtName.getId();
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
                nameMap.put(cxtName.getName(), cxtName);
                version.getNameAdded().add(name);
            }
            cxtName.flushRTime();
        }
    }

}
