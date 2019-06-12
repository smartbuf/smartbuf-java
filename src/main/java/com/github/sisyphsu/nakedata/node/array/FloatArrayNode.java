package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.type.DataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author sulin
 * @since 2019-06-11 20:32:34
 */
public class FloatArrayNode extends ArrayNode {

    public static final FloatArrayNode NULL = new FloatArrayNode(null);
    public static final FloatArrayNode EMPTY = new FloatArrayNode(new ArrayList<>());

    private List<Float> items;

    private FloatArrayNode(List<Float> items) {
        this.items = items;
    }

    public static FloatArrayNode valueOf(Float b) {
        List<Float> items = new ArrayList<>();
        items.add(b);
        return new FloatArrayNode(items);
    }

    public static FloatArrayNode valueOf(Collection<Float> b) {
        return new FloatArrayNode(new ArrayList<>(b));
    }

    @Override
    public int size() {
        return items == null ? 0 : items.size();
    }

    @Override
    public boolean tryAppend(Object o) {
        return false;
    }

    @Override
    public DataType elementDataType() {
        return DataType.FLOAT;
    }

}