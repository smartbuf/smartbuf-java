package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.type.DataType;

import java.util.List;

/**
 * @author sulin
 * @since 2019-05-08 21:02:18
 */
public class ArrayNode extends AbstractNode {

    public final static ArrayNode NULL = new ArrayNode(null);

    private final List<AbstractNode> children;

    private ArrayNode(List<AbstractNode> children) {
        this.children = children;
    }

    public static ArrayNode valueOf(List<AbstractNode> nodes) {
        if (nodes == null)
            return NULL;
        return new ArrayNode(nodes);
    }

    @Override
    public DataType getType() {
        return DataType.ARRAY;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

}
