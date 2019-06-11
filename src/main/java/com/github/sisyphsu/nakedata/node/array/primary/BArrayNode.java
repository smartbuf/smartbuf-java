package com.github.sisyphsu.nakedata.node.array.primary;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;

/**
 * byte[] array
 *
 * @author sulin
 * @since 2019-05-08 21:01:31
 */
public class BArrayNode extends ArrayNode {

    public static final BArrayNode NULL = new BArrayNode(null);
    public static final BArrayNode EMPTY = new BArrayNode(new byte[0]);

    private byte[] items;

    private BArrayNode(byte[] items) {
        this.items = items;
    }

    public static BArrayNode valueOf(byte[] items) {
        if (items == null) {
            return NULL;
        }
        if (items.length == 0) {
            return EMPTY;
        }
        return new BArrayNode(items);
    }

    @Override
    public int size() {
        return items.length;
    }

    @Override
    public ContextType contextType() {
        return null;
    }

}
