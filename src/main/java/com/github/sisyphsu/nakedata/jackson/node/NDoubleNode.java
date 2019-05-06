package com.github.sisyphsu.nakedata.jackson.node;

import com.fasterxml.jackson.databind.node.DoubleNode;
import com.github.sisyphsu.nakedata.DataType;

/**
 * @author sulin
 * @since 2019-05-06 12:17:08
 */
public class NDoubleNode extends DoubleNode implements DataType {

    protected NDoubleNode(double v) {
        super(v);
    }

    public static DoubleNode valueOf(double v) {
        return new NDoubleNode(v);
    }

    @Override
    public byte getTypeCode() {
        return DOUBLE;
    }

}
