package com.github.sisyphsu.nakedata.jackson.node;

import com.fasterxml.jackson.databind.node.BooleanNode;
import com.github.sisyphsu.nakedata.DataType;

/**
 * @author sulin
 * @since 2019-05-06 11:25:13
 */
public class NBooleanNode extends BooleanNode implements DataType {

    public final static NBooleanNode TRUE = new NBooleanNode(true);
    public final static NBooleanNode FALSE = new NBooleanNode(false);

    protected NBooleanNode(boolean v) {
        super(v);
    }

    @Override
    public byte getTypeCode() {
        if (this == TRUE)
            return DataType.TRUE;
        if (this == FALSE)
            return DataType.FALSE;
        throw new IllegalStateException("Illegal BooleanNode");
    }

}
