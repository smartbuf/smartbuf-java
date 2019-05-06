package com.github.sisyphsu.nakedata.jackson.node;

import com.fasterxml.jackson.databind.node.TextNode;
import com.github.sisyphsu.nakedata.DataType;

/**
 * @author sulin
 * @since 2019-05-06 16:56:48
 */
public class NTextNode extends TextNode implements DataType {

    final static NTextNode EMPTY_STRING_NODE = new NTextNode("");

    protected NTextNode(String v) {
        super(v);
    }

    public static NTextNode valueOf(String v) {
        if (v == null) {
            return null;
        }
        if (v.length() == 0) {
            return EMPTY_STRING_NODE;
        }
        return new NTextNode(v);
    }

    @Override
    public byte getTypeCode() {
        return STRING;
    }

}
