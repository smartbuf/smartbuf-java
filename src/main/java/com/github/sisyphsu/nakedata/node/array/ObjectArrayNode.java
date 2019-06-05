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

    private ObjectNode[] items;

    public ObjectArrayNode(ObjectNode[] items) {
        if (items == null || items.length == 0) {
            throw new IllegalArgumentException("items can't be null or empty");
        }
        ContextType type = items[0].getContextType();
        for (int i = 1; i < items.length; i++) {
            if (type != items[i].getContextType()) {
                throw new IllegalArgumentException("items ContextType must be the same");
            }
        }
        this.items = items;
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
