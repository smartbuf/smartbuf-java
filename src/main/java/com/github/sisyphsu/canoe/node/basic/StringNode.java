package com.github.sisyphsu.canoe.node.basic;

import com.github.sisyphsu.canoe.node.Node;

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
    public Object value() {
        return value;
    }

    @Override
    public Type type() {
        return Type.STRING;
    }

}
