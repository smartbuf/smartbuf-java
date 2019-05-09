package com.github.sisyphsu.nakedata.node;

import java.math.BigInteger;

/**
 * @author sulin
 * @since 2019-05-08 21:01:44
 */
public class BigIntegerNode extends AbstractNode {

    public final static BigIntegerNode NULL = new BigIntegerNode();
    public final static BigIntegerNode ZERO = new BigIntegerNode(BigInteger.ZERO);

    private final boolean nil;
    private final BigInteger value;

    private BigIntegerNode() {
        this.nil = true;
        this.value = null;
    }

    private BigIntegerNode(BigInteger value) {
        this.nil = false;
        this.value = value;
    }

    public static BigIntegerNode valueOf(BigInteger value) {
        if (value == null)
            return NULL;
        if (value.equals(BigInteger.ZERO)) {
            return ZERO;
        }
        return new BigIntegerNode(value);
    }

}
