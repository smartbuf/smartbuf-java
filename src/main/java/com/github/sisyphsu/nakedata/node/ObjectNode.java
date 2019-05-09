package com.github.sisyphsu.nakedata.node;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author sulin
 * @since 2019-05-08 21:02:12
 */
public class ObjectNode extends AbstractNode {

    public final static ObjectNode NULL = new ObjectNode();

    private final boolean nil;
    private final TreeMap<String, AbstractNode> children;

    private ObjectNode() {
        this.nil = true;
        this.children = null;
    }

    private ObjectNode(TreeMap<String, AbstractNode> children) {
        this.nil = false;
        this.children = children;
    }

    public static ObjectNode newInstance(Map<String, AbstractNode> children) {
        if (children == null) {
            return NULL;
        }
        if (children instanceof TreeMap) {
            return new ObjectNode((TreeMap<String, AbstractNode>) children);
        }
        return new ObjectNode(new TreeMap<>(children));
    }

}
