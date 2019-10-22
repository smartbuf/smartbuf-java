package com.github.sisyphsu.datube.node.std;

import com.github.sisyphsu.datube.node.Node;
import com.github.sisyphsu.datube.node.NodeType;

/**
 * SymbolNode represents constant String or Enum etc.
 *
 * @author sulin
 * @since 2019-06-04 20:23:31
 */
public final class SymbolNode extends Node {

    private String data;

    private SymbolNode(String data) {
        this.data = data;
    }

    public static SymbolNode valueOf(Enum en) {
        return new SymbolNode(en.name()); // don't need cache
    }

    public static SymbolNode valueOf(String data) {
        return new SymbolNode(data);
    }

    @Override
    public NodeType type() {
        return NodeType.SYMBOL;
    }

    @Override
    public String stringValue() {
        return data;
    }

}
