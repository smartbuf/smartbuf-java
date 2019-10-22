package com.github.sisyphsu.datube.convertor;

import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author sulin
 * @since 2019-10-22 20:41:53
 */
public final class CodecState {

    private static final Object NULL = new Object();

    private static final ThreadLocal<CodecState> STATE_LOCAL = new ThreadLocal<>();

    private int                 depth;
    private Map<Object, Object> objectMap;

    public static void reset() {
        STATE_LOCAL.remove();
    }

    public static CodecState get() {
        CodecState state = STATE_LOCAL.get();
        if (state == null) {
            state = new CodecState();
            STATE_LOCAL.set(state);
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

    @Override
    public String toString() {
        return String.format("%d, %d", depth, objectMap == null ? 0 : objectMap.size());
    }
}
