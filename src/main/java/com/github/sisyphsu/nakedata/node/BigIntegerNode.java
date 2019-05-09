package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.type.DataType;

import java.math.BigInteger;

/**
 * @author sulin
 * @since 2019-05-08 21:01:44
 */
public class BigIntegerNode extends AbstractNode {

    public final static BigIntegerNode NULL = new BigIntegerNode(null);
    public final static BigIntegerNode ZERO = new BigIntegerNode(BigInteger.ZERO);

    private final BigInteger value;

    private BigIntegerNode(BigInteger value) {
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

    @Override
    public DataType getType() {
        return DataType.BIGINTEGER;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

}
