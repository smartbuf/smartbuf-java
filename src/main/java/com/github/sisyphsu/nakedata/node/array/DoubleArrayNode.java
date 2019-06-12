package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.type.DataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author sulin
 * @since 2019-06-11 20:32:23
 */
public class DoubleArrayNode extends ArrayNode {

    public static final DoubleArrayNode NULL = new DoubleArrayNode(null);
    public static final DoubleArrayNode EMPTY = new DoubleArrayNode(new ArrayList<>());

    private List<Double> items;

    private DoubleArrayNode(List<Double> items) {
        this.items = items;
    }

    public static DoubleArrayNode valueOf(Double b) {
        List<Double> items = new ArrayList<>();
        items.add(b);
        return new DoubleArrayNode(items);
    }

    public static DoubleArrayNode valueOf(Collection<Double> b) {
        return new DoubleArrayNode(new ArrayList<>(b));
    }

    @Override
    public int size() {
        return items == null ? 0 : items.size();
    }

    @Override
    public boolean tryAppend(Object o) {
        if (o instanceof Double) {
            items.add((Double) o);
            return true;
        }
        return false;
    }

    @Override
    public DataType elementDataType() {
        return DataType.DOUBLE;
    }

}
