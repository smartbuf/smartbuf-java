package com.github.sisyphsu.canoe.convertor;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * CodecContext used for recording context.
 * It helps codec recording call depth to prevent infinity loop.
 *
 * @author sulin
 * @since 2019-10-22 20:41:53
 */
public final class CodecContext {

    private static final Object                    NULL  = new Object();
    private static final ThreadLocal<CodecContext> LOCAL = new ThreadLocal<>();

    private int                 depth;
    private Map<Object, Object> objectMap;

    /**
     * Reset the current thread's codec-context, should be called at the very first.
     */
    public static void reset() {
        LOCAL.remove();
    }

    /**
     * Fetch the current thread's codec-context
     */
    public static CodecContext get() {
        CodecContext state = LOCAL.get();
        if (state == null) {
            state = new CodecContext();
            LOCAL.set(state);
        }
        return state;
    }

    public int depth() {
        return ++depth;
    }

    public boolean record(Object obj) {
        if (objectMap == null) {
            objectMap = new IdentityHashMap<>();
        }
        if (objectMap.containsKey(obj)) {
            return false;
        }
        objectMap.put(obj, NULL);
        return true;
    }

}
