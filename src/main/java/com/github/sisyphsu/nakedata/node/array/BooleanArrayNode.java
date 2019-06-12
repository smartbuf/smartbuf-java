package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.type.DataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author sulin
 * @since 2019-06-11 20:32:06
 */
public class BooleanArrayNode extends ArrayNode {

    public static final BooleanArrayNode NULL = new BooleanArrayNode(null);
    public static final BooleanArrayNode EMPTY = new BooleanArrayNode(new ArrayList<>());

    private List<Boolean> items;

    private BooleanArrayNode(List<Boolean> items) {
        this.items = items;
    }

    public static BooleanArrayNode valueOf(Boolean b) {
        List<Boolean> items = new ArrayList<>();
        items.add(b);
        return new BooleanArrayNode(items);
    }

    public static BooleanArrayNode valueOf(Collection<Boolean> b) {
        return new BooleanArrayNode(new ArrayList<>(b));
    }

    @Override
    public int size() {
        return items == null ? 0 : items.size();
    }

    @Override
    public boolean tryAppend(Object o) {
        if (o instanceof Boolean) {
            this.items.add((Boolean) o);
            return true;
        }
        return false;
    }

    @Override
    public DataType elementDataType() {
        return DataType.BOOL;
    }

}
