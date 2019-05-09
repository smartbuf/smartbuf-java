package com.github.sisyphsu.nakedata.node;

import java.math.BigDecimal;

/**
 * @author sulin
 * @since 2019-05-08 21:01:56
 */
public class BigDecimalNode extends AbstractNode {

    public final static BigDecimalNode NULL = new BigDecimalNode();
    public final static BigDecimalNode ZERO = new BigDecimalNode(BigDecimal.ZERO);

    private final boolean nil;
    private final BigDecimal value;

    private BigDecimalNode() {
        this.nil = true;
        this.value = null;
    }

    private BigDecimalNode(BigDecimal value) {
        this.nil = false;
        this.value = value;
    }

    public static BigDecimalNode valueOf(BigDecimal value) {
        if (value == null)
            return NULL;
        if (BigDecimal.ZERO.compareTo(value) == 0) {
            return ZERO;
        }
        return new BigDecimalNode(value);
    }

}
