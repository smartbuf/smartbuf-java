package com.github.sisyphsu.nakedata.node.std;

import com.github.sisyphsu.nakedata.DataType;
import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.Node;

import java.util.TreeMap;

/**
 * ObjectNode represents JavaBean or Map.
 *
 * @author sulin
 * @since 2019-05-08 21:02:12
 */
public final class ObjectNode extends Node {

    public final static ObjectNode NULL  = new ObjectNode(null);
    public final static ObjectNode EMPTY = new ObjectNode(new TreeMap<>());

    private final TreeMap<String, Node> fields;

    private ContextType contextType;

    private ObjectNode(TreeMap<String, Node> fields) {
        this.fields = fields;
    }

    public static ObjectNode valueOf(TreeMap<String, Node> fields) {
        if (fields == null) {
            return NULL;
        }
        if (fields.isEmpty()) {
            return EMPTY;
        }
        return new ObjectNode(fields);
    }

    @Override
    public DataType dataType() {
        return DataType.OBJECT;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

    public int size() {
        return fields.size();
    }

    public TreeMap<String, Node> getFields() {
        return fields;
    }

    public ContextType getContextType() {
        return contextType;
    }

    public void setContextType(ContextType contextType) {
        this.contextType = contextType;
    }

}
