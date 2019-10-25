package com.github.sisyphsu.datatube.node.std;

import com.github.sisyphsu.datatube.node.Node;
import com.github.sisyphsu.datatube.node.NodeType;

/**
 * StringNode represents String.
 *
 * @author sulin
 * @since 2019-05-08 21:00:34
 */
public final class StringNode extends Node {

    public final static StringNode EMPTY = new StringNode("");

    private final String value;

    private StringNode(String value) {
        this.value = value;
    }

    public static StringNode valueOf(String str) {
        if (str.isEmpty()) {
            return EMPTY;
        }
        return new StringNode(str);
    }

    @Override
    public NodeType type() {
        return NodeType.STRING;
    }

    @Override
    public String stringValue() {
        return value;
    }
}
