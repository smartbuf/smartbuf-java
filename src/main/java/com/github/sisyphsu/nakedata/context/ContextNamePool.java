package com.github.sisyphsu.nakedata.context;

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

    private int limit;

    private IDPool pool;

    private Map<String, ContextName> nameIdMap = new HashMap<>();

    public ContextNamePool(int limit) {
        this.limit = limit;
        this.keep = (int) (limit * FACTOR_KEEP);
        this.pool = new IDPool(limit);
    }

    public void addNames(ContextVersion version, Set<String> names) {
        // if reach limit, expire some low priority name.
        if (names.size() + nameIdMap.size() > limit) {
            // todo release some unactive
            ContextName[] dead = new ContextName[limit - keep];
            for (ContextName name : nameIdMap.values()) {
                if (names.contains(name.getName())){
                    continue;
                }

            }
        }
        // only add the name which didn't exists.

    }

    private void doRelease() {

    }

}
