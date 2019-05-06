package com.github.sisyphsu.nakedata.jackson.node;

import com.fasterxml.jackson.databind.node.LongNode;
import com.github.sisyphsu.nakedata.DataType;

/**
 * @author sulin
 * @since 2019-05-06 12:16:22
 */
public class NVarintNode extends LongNode implements DataType {

    protected NVarintNode(long v) {
        super(v);
    }

    public static NVarintNode valueOf(long l) {
        return new NVarintNode(l);
    }

    @Override
    public byte getTypeCode() {
        return NUMBER;
    }

}
