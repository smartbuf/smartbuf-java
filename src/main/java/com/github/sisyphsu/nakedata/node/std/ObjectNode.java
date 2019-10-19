package com.github.sisyphsu.nakedata.node.std;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.NodeType;

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

    public final static ObjectNode NULL  = new ObjectNode(true, null, null);
    public final static ObjectNode EMPTY = new ObjectNode(true, new String[0], Collections.EMPTY_MAP);

    private final boolean           stable;
    private final String[]          fields;
    private final Map<String, Node> data;

    public ObjectNode(boolean stable, String[] fields, Map<String, Node> data) {
        this.stable = stable;
        this.fields = fields;
        this.data = data;
    }

    public static ObjectNode valueOf(boolean stable, String[] fields, Map<String, Node> map) {
        if (map == null) {
            return NULL;
        }
        if (map.isEmpty()) {
            return EMPTY;
        }
        return new ObjectNode(stable, fields, map);
    }

    @Override
    public NodeType dataType() {
        return NodeType.OBJECT;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

    public int size() {
        return data.size();
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
