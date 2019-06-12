package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.type.DataType;

/**
 * null array, only record count.
 *
 * @author sulin
 * @since 2019-06-05 16:07:43
 */
public class NullArrayNode extends ArrayNode {

    private int count;

    private NullArrayNode(int count) {
        this.count = count;
    }

    public static NullArrayNode valueOf(int count) {
        return new NullArrayNode(count);
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public boolean tryAppend(Object o) {
        if (o == null) {
            count++;
            return true;
        }
        return false;
    }

    @Override
    public DataType elementDataType() {
        return DataType.NULL;
    }

}
