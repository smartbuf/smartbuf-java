package com.github.sisyphsu.canoe.node.basic;

import com.github.sisyphsu.canoe.Const;
import com.github.sisyphsu.canoe.node.Node;

/**
 * BooleanNode represents boolean and Boolean.
 *
 * @author sulin
 * @since 2019-05-08 21:00:07
 */
public final class BooleanNode extends Node {

    public final static BooleanNode TRUE  = new BooleanNode();
    public final static BooleanNode FALSE = new BooleanNode();

    private BooleanNode() {
    }

    public static BooleanNode valueOf(boolean b) {
        return b ? TRUE : FALSE;
    }

    public static BooleanNode valueOf(Boolean b) {
        return valueOf(b.booleanValue());
    }

    @Override
    public Object value() {
        return this == TRUE;
    }

    @Override
    public byte type() {
        return Const.TYPE_CONST;
    }

}
