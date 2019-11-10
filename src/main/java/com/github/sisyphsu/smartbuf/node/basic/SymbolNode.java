package com.github.sisyphsu.smartbuf.node.basic;

import com.github.sisyphsu.smartbuf.node.Node;
import com.github.sisyphsu.smartbuf.node.NodeType;

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

    public static SymbolNode valueOf(String str) {
        return new SymbolNode(str);
    }

    public static SymbolNode valueOf(Enum en) {
        return new SymbolNode(en.name()); // don't need cache
    }

    @Override
    public Object value() {
        return data;
    }

    @Override
    public NodeType type() {
        return NodeType.SYMBOL;
    }
}
