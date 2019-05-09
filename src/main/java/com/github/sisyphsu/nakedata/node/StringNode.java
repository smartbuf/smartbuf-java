package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.type.DataType;

/**
 * @author sulin
 * @since 2019-05-08 21:00:34
 */
public class StringNode extends AbstractNode {

    public final static StringNode NULL = new StringNode(null);
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
    public DataType getType() {
        return DataType.STRING;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

}
