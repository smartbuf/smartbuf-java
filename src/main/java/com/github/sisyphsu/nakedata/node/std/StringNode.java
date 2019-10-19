package com.github.sisyphsu.nakedata.node.std;

import com.github.sisyphsu.nakedata.node.NodeType;
import com.github.sisyphsu.nakedata.node.Node;

/**
 * StringNode represents String.
 *
 * @author sulin
 * @since 2019-05-08 21:00:34
 */
public final class StringNode extends Node {

    public final static StringNode NULL  = new StringNode(null);
    public final static StringNode EMPTY = new StringNode("");

    private final String value;

    private StringNode(String value) {
        this.value = value;
    }

    public static StringNode valueOf(String str) {
        if (str == null) {
            return NULL;
        }
        if (str.isEmpty()) {
            return EMPTY;
        }
        return new StringNode(str);
    }

    @Override
    public NodeType dataType() {
        return NodeType.STRING;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

    @Override
    public String stringValue() {
        return value;
    }
}
