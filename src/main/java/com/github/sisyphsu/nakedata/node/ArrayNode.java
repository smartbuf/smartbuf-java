package com.github.sisyphsu.nakedata.node;

import java.util.List;

/**
 * @author sulin
 * @since 2019-05-08 21:02:18
 */
public class ArrayNode extends AbstractNode {

    public final static ArrayNode NULL = new ArrayNode();

    private final boolean nil;
    private final List<AbstractNode> children;

    private ArrayNode() {
        this.nil = true;
        this.children = null;
    }

    private ArrayNode(List<AbstractNode> children) {
        this.nil = false;
        this.children = children;
    }

    public static ArrayNode valueOf(List<AbstractNode> nodes) {
        if (nodes == null)
            return NULL;
        return new ArrayNode(nodes);
    }

}
