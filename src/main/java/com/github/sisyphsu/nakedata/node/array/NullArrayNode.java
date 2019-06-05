package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * null array, only record count.
 *
 * @author sulin
 * @since 2019-06-05 16:07:43
 */
public class NullArrayNode extends ArrayNode {

    private static final NullArrayNode[] TABLE = new NullArrayNode[16];

    static {
        for (int i = 0; i < TABLE.length; i++) {
            TABLE[i] = new NullArrayNode(i);
        }
    }

    private int count;

    private NullArrayNode(int count) {
        this.count = count;
    }

    public static NullArrayNode valueOf(int count) {
        if (count < TABLE.length) {
            return TABLE[count];
        }
        return new NullArrayNode(count);
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public DataType dataType() {
        return DataType.NULL;
    }

    @Override
    public ContextType contextType() {
        return null;
    }

}
