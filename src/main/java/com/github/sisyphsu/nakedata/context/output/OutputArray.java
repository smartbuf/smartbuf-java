package com.github.sisyphsu.nakedata.context.output;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sulin
 * @since 2019-09-26 14:21:44
 */
@SuppressWarnings("unchecked")
public final class OutputArray<T> {

    private       int             offset;
    private       Object[]        items     = new Object[4];
    private final Map<T, Integer> offsetMap = new HashMap<>();

    public void add(T t) {
        int id = offset;
        this.add(id, t);
    }

    public void add(int id, T t) {
        if (t == null) {
            throw new NullPointerException();
        }
        if (items.length < id) {
            Object[] newItems = new Object[items.length * 2];
            System.arraycopy(items, 0, newItems, 0, offset);
            items = newItems;
        }
        if (offset < id) {
            offset = id;
        }
        items[id] = t;
        offsetMap.put(t, id);
    }

    public void remove(int id) {
        T t = (T) items[id];
        if (t == null) {
            throw new IllegalArgumentException(id + " not exists");
        }
        offsetMap.remove(t);
    }

    public T get(int offset) {
        return (T) items[offset];
    }

    public Integer getID(T t) {
        Integer index = offsetMap.get(t);
        if (index == null) {
            throw new RuntimeException("BUG: unregistered " + t);
        }
        return index;
    }

    public boolean contains(T t) {
        return offsetMap.containsKey(t);
    }

    public int size() {
        return offsetMap.size();
    }

    public T[] data() {
        return (T[]) items;
    }

}
