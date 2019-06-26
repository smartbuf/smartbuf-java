package com.github.sisyphsu.nakedata.node.std;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * @author sulin
 * @since 2019-05-08 21:00:07
 */
public class BooleanNode extends Node {

    public final static BooleanNode NULL = new BooleanNode();

    public final static BooleanNode TRUE = new BooleanNode();
    public final static BooleanNode FALSE = new BooleanNode();

    private BooleanNode() {
    }

    public static BooleanNode valueOf(boolean b) {
        return b ? TRUE : FALSE;
    }

    public static BooleanNode valueOf(Boolean b) {
        if (b == null)
            return NULL;
        return valueOf(b.booleanValue());
    }

    @Override
    public DataType dataType() {
        return DataType.BOOL;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

    public Boolean value() {
        if (isNull()) {
            return null;
        }
        return this == TRUE;
    }

}
