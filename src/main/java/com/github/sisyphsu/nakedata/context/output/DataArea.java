package com.github.sisyphsu.nakedata.context.output;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author sulin
 * @since 2019-09-25 20:21:26
 */
public final class DataArea<T> {

    protected int count = 0;
    protected final ArrayList<T> values = new ArrayList<>();
    protected final HashMap<T, Integer> valueMap = new HashMap<>();

    public void add(T t) {
        if (valueMap.containsKey(t)) {
            return;
        }
        valueMap.put(t, count++);
        values.add(t);
    }

    public int get(T t) {
        Integer index = this.valueMap.get(t);
        if (index == null) {
            throw new RuntimeException("BUG: unregistered " + t);
        }
        return index;
    }

}
