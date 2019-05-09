package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.type.DataType;

import java.math.BigDecimal;

/**
 * @author sulin
 * @since 2019-05-08 21:01:56
 */
public class BigDecimalNode extends AbstractNode {

    public final static BigDecimalNode NULL = new BigDecimalNode(null);
    public final static BigDecimalNode ZERO = new BigDecimalNode(BigDecimal.ZERO);

    private final BigDecimal value;

    private BigDecimalNode(BigDecimal value) {
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

    @Override
    public DataType getType() {
        return DataType.BIGDECIMAL;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

}
