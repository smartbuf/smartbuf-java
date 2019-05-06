package com.github.sisyphsu.nakedata.jackson.node;

import com.fasterxml.jackson.databind.node.FloatNode;
import com.github.sisyphsu.nakedata.DataType;

/**
 * @author sulin
 * @since 2019-05-06 16:54:31
 */
public class NFloatNode extends FloatNode implements DataType {

    protected NFloatNode(float v) {
        super(v);
    }

    public static FloatNode valueOf(float v) {
        return new NFloatNode(v);
    }

    @Override
    public byte getTypeCode() {
        return FLOAT;
    }

}
