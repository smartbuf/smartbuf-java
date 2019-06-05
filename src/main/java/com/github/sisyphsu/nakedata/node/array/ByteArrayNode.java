package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * byte[] array
 *
 * @author sulin
 * @since 2019-05-08 21:01:31
 */
public class ByteArrayNode extends ArrayNode {

    public static final ByteArrayNode NULL = new ByteArrayNode(null);
    public static final ByteArrayNode EMPTY = new ByteArrayNode(new byte[0]);

    private byte[] items;

    private ByteArrayNode(byte[] items) {
        this.items = items;
    }

    public static ByteArrayNode valueOf(byte[] items) {
        if (items == null) {
            return NULL;
        }
        if (items.length == 0) {
            return EMPTY;
        }
        return new ByteArrayNode(items);
    }

    @Override
    public int size() {
        return items.length;
    }

    @Override
    public DataType dataType() {
        return DataType.BYTE;
    }

    @Override
    public ContextType contextType() {
        return null;
    }

}
