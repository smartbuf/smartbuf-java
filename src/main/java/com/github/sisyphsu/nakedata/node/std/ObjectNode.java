package com.github.sisyphsu.nakedata.node.std;

import com.github.sisyphsu.nakedata.DataType;
import com.github.sisyphsu.nakedata.node.Node;

import java.util.Collections;
import java.util.Map;

/**
 * ObjectNode represents JavaBean or Map.
 * TODO 建立与struct的关系
 *
 * @author sulin
 * @since 2019-05-08 21:02:12
 */
@SuppressWarnings("unchecked")
public final class ObjectNode extends Node {

    public final static ObjectNode NULL  = new ObjectNode(null, null);
    public final static ObjectNode EMPTY = new ObjectNode(new Key(true, new String[0]), Collections.EMPTY_MAP);

    private final Key               key;
    private final Map<String, Node> data;

    private ObjectNode(Key key, Map<String, Node> map) {
        this.key = key;
        this.data = map;
    }

    public static ObjectNode valueOf(Key key, Map<String, Node> map) {
        if (map == null) {
            return NULL;
        }
        if (map.isEmpty()) {
            return EMPTY;
        }
        return new ObjectNode(key, map);
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
        return data.size();
    }

    public Map<String, Node> getData() {
        return data;
    }

    public Key getKey() {
        return key;
    }

    public String[] getFields() {
        return key.fields;
    }

    public Node getField(String name) {
        return data.get(name);
    }

    public boolean isStable() {
        return key.stable;
    }

    public static final class Key {

        private final boolean  stable;
        private final String[] fields;

        public Key(boolean stable, String[] fields) {
            this.stable = stable;
            this.fields = fields;
        }

        public boolean isStable() {
            return stable;
        }

        public String[] getFields() {
            return fields;
        }
    }

}
