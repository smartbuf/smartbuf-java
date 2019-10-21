package com.github.sisyphsu.datube.node.std;

import com.github.sisyphsu.datube.node.Node;
import com.github.sisyphsu.datube.node.NodeType;

import java.util.Collections;
import java.util.Map;

/**
 * ObjectNode represents JavaBean or Map.
 *
 * @author sulin
 * @since 2019-05-08 21:02:12
 */
@SuppressWarnings("unchecked")
public final class ObjectNode extends Node {

    public final static ObjectNode EMPTY = new ObjectNode(true, new String[0], Collections.EMPTY_MAP);

    private final boolean           stable;
    private final String[]          fields;
    private final Map<String, Node> data;

    public ObjectNode(boolean stable, String[] fields, Map<String, Node> data) {
        this.stable = stable;
        this.fields = fields;
        this.data = data;
    }

    @Override
    public NodeType type() {
        return NodeType.OBJECT;
    }

    public Map<String, Node> getData() {
        return data;
    }

    public String[] getFields() {
        return fields;
    }

    public Node getField(String name) {
        return data.get(name);
    }

    public boolean isStable() {
        return stable;
    }

}
