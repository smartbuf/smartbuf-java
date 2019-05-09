package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.type.DataType;

/**
 * @author sulin
 * @since 2019-05-08 21:03:54
 */
public class NullNode extends AbstractNode {

    public final static NullNode INSTANCE = new NullNode();

    private NullNode() {
    }

    @Override
    public DataType getType() {
        return DataType.NULL;
    }

    @Override
    public boolean isNull() {
        return true;
    }

}
