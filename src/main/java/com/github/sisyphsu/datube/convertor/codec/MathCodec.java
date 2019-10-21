package com.github.sisyphsu.datube.convertor.codec;

import com.github.sisyphsu.datube.convertor.Converter;
import com.github.sisyphsu.datube.convertor.Codec;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Codec for java.math package, include BigDecimal and BigInteger.
 *
 * @author sulin
 * @since 2019-07-25 12:17:16
 */
public final class MathCodec extends Codec {

    /**
     * Convert String to BigDecimal
     */
    @Converter
    public BigDecimal toBigDecimal(String s) {
        return new BigDecimal(s);
    }

    /**
     * Convert BigDecimal to String
     */
    @Converter
    public String toString(BigDecimal bd) {
        return bd.toPlainString();
    }

    /**
     * Convert String to BigInteger
     */
    @Converter
    public BigInteger toBigInteger(String s) {
        return new BigInteger(s);
    }

    /**
     * Convert BigInteger to String
     */
    @Converter
    public String toString(BigInteger bi) {
        return bi.toString();
    }

}
