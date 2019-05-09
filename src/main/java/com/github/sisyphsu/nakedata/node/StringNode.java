package com.github.sisyphsu.nakedata.node;

/**
 * @author sulin
 * @since 2019-05-08 21:00:34
 */
public class StringNode extends AbstractNode {

    public final static StringNode NULL = new StringNode();
    public final static StringNode EMPTY = new StringNode("");

    private final boolean nil;
    private final String value;

    private StringNode() {
        this.nil = true;
        this.value = null;
    }

    private StringNode(String value) {
        this.nil = false;
        this.value = value;
    }

    public static StringNode valueOf(String str) {
        if (str == null) {
            return NULL;
        }
        if (str.isEmpty()) {
            return EMPTY;
        }
        // 虚引用
        return new StringNode(str);
    }

}
