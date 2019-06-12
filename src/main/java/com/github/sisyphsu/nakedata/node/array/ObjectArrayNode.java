package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.std.ObjectNode;
import com.github.sisyphsu.nakedata.type.DataType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * object[] array, every item's ContextType must be the same
 *
 * @author sulin
 * @since 2019-06-05 16:06:44
 */
public class ObjectArrayNode extends ArrayNode {

    private ContextType type;
    private List<ObjectNode> items;

    private ObjectArrayNode(List<ObjectNode> items) {
        if (items.size() > 0) {
            this.type = items.get(0).getContextType();
        }
        for (ObjectNode item : items) {
            if (this.type == null) {
                this.type = item.getContextType();
            } else if (this.type != item.getContextType()) {
                throw new IllegalArgumentException("ContextType uncompatible");
            }
        }
        this.items = items;
    }

    public static ObjectArrayNode valueOf(Collection<ObjectNode> items) {
        List<ObjectNode> arr = new ArrayList<>();
        if (items != null) {
            arr.addAll(items);
        }
        return new ObjectArrayNode(arr);
    }

    @Override
    public int size() {
        return items == null ? 0 : items.size();
    }

    @Override
    public boolean tryAppend(Object o) {
        if (!(o instanceof ObjectNode)) {
            return false;
        }
        ObjectNode node = (ObjectNode) o;
        if (node.getContextType() != this.type) {
            return false;
        }
        this.items.add(node);

        return true;
    }

    @Override
    public DataType elementDataType() {
        return DataType.OBJECT;
    }

    @Override
    public Object elementContextType() {
        return type;
    }

}
