package com.github.sisyphsu.nakedata.global;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author sulin
 * @since 2019-04-29 13:24:57
 */
public class GlobalName {

    private static final Map<String, GlobalName> CACHE = new ConcurrentHashMap<>();

    /**
     * Name's real value
     */
    private String value;
    /**
     * Reference count, used for gc
     */
    private AtomicInteger refCount;

    private GlobalName(String value) {
        this.value = value;
    }

    public static GlobalName valueOf(String name) {
        return CACHE.putIfAbsent(name, new GlobalName(name));
    }

    public void require() {
        refCount.incrementAndGet();
    }

    public void release() {
        refCount.decrementAndGet();
    }

}
