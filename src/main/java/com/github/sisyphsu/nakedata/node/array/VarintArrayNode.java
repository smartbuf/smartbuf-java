package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.type.DataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author sulin
 * @since 2019-06-11 20:32:52
 */
public class VarintArrayNode extends ArrayNode {

    public static final VarintArrayNode NULL = new VarintArrayNode(null);
    public static final VarintArrayNode EMPTY = new VarintArrayNode(new ArrayList<>());

    private List<Long> items;

    private VarintArrayNode(List<Long> items) {
        this.items = items;
    }

    public static VarintArrayNode valueOf(Long b) {
        List<Long> items = new ArrayList<>();
        items.add(b);
        return new VarintArrayNode(items);
    }

    public static VarintArrayNode valueOf(Collection<Long> b) {
        return new VarintArrayNode(new ArrayList<>(b));
    }

    public static VarintArrayNode valueOf(Long[] data) {
        return new VarintArrayNode(Arrays.asList(data));
    }

    @Override
    public int size() {
        return items == null ? 0 : items.size();
    }

    @Override
    public boolean tryAppend(Object o) {
        if (o instanceof Byte) {
            items.add(Long.valueOf((Byte) o));
        } else if (o instanceof Short) {
            items.add(Long.valueOf((Short) o));
        } else if (o instanceof Integer) {
            items.add(Long.valueOf((Integer) o));
        } else if (o instanceof Long) {
            items.add((Long) o);
        } else {
            return false;
        }
        return true;
    }

    @Override
    public DataType elementDataType() {
        return DataType.VARINT;
    }

}
