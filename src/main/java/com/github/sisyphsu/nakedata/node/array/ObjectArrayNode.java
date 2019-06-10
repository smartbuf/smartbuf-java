package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.std.ObjectNode;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * object[] array, every item's ContextType must be the same
 *
 * @author sulin
 * @since 2019-06-05 16:06:44
 */
public class ObjectArrayNode extends ArrayNode {

    public static final ObjectArrayNode NULL = new ObjectArrayNode(null);
    public static final ObjectArrayNode EMPTY = new ObjectArrayNode(new ObjectNode[0]);

    private ObjectNode[] items;

    private ObjectArrayNode(ObjectNode[] items) {
        this.items = items;
    }

    public static ObjectArrayNode valueOf(ObjectNode[] items) {
        if (items == null) {
            return NULL;
        }
        if (items.length == 0) {
            return EMPTY;
        }
        return new ObjectArrayNode(items);
    }

    @Override
    public int size() {
        return items.length;
    }

    @Override
    public DataType dataType() {
        return DataType.OBJECT;
    }

    @Override
    public ContextType contextType() {
        return items[0].getContextType();
    }

}
