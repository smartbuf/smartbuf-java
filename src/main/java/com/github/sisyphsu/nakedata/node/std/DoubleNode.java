package com.github.sisyphsu.nakedata.node.std;

import com.github.sisyphsu.nakedata.DataType;
import com.github.sisyphsu.nakedata.node.Node;

/**
 * DoubleNode represents double and Double.
 *
 * @author sulin
 * @since 2019-05-08 21:00:27
 */
public final class DoubleNode extends Node {

    public final static DoubleNode NULL = new DoubleNode(0);
    public final static DoubleNode ZERO = new DoubleNode(0);

    private final double value;

    private DoubleNode(double value) {
        this.value = value;
    }

    public static DoubleNode valueOf(double d) {
        if (d == 0) {
            return ZERO;
        }
        return new DoubleNode(d);
    }

    public static DoubleNode valueOf(Double d) {
        if (d == null) {
            return NULL;
        }
        return valueOf(d.doubleValue());
    }

    @Override
    public double doubleValue() {
        return value;
    }

    @Override
    public DataType dataType() {
        return DataType.DOUBLE;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

}
