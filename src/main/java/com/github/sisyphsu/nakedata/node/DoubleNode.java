package com.github.sisyphsu.nakedata.node;

/**
 * @author sulin
 * @since 2019-05-08 21:00:27
 */
public class DoubleNode extends AbstractNode {

    public final static DoubleNode NULL = new DoubleNode();
    public final static DoubleNode ZERO = new DoubleNode(0);

    private final boolean nil;
    private final double value;

    private DoubleNode() {
        this.nil = true;
        this.value = 0;
    }

    private DoubleNode(double value) {
        this.nil = false;
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

}
