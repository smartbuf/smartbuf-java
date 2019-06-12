package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.type.DataType;

import java.util.Arrays;
import java.util.List;

/**
 * string[] array, can't contains null
 *
 * @author sulin
 * @since 2019-06-04 16:52:57
 */
public class StringArrayNode extends ArrayNode {

    public static final StringArrayNode NULL = new StringArrayNode(null);

    private List<String> items;

    private StringArrayNode(List<String> items) {
        this.items = items;
    }

    public static StringArrayNode valueOf(String[] items) {
        if (items == null) {
            return NULL;
        }
        return new StringArrayNode(Arrays.asList(items));
    }

    @Override
    public int size() {
        return items == null ? 0 : items.size();
    }

    @Override
    public boolean tryAppend(Object o) {
        if (o instanceof String) {
            this.items.add((String) o);
            return true;
        }
        return false;
    }

    @Override
    public DataType elementDataType() {
        return DataType.STRING;
    }

}
