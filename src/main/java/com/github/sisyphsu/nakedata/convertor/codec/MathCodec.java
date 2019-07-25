package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Codec for java.math package, include BigDecimal and BigInteger.
 *
 * @author sulin
 * @since 2019-07-25 12:17:16
 */
public class MathCodec extends Codec {

    /**
     * Convert String to BigDecimal
     */
    @Converter
    public BigDecimal toBigDecimal(String s) {
        return s == null ? null : new BigDecimal(s);
    }

    /**
     * Convert BigDecimal to String
     */
    @Converter
    public String toString(BigDecimal bd) {
        return bd == null ? null : bd.toPlainString();
    }

    /**
     * Convert String to BigInteger
     */
    @Converter
    public BigInteger toBigInteger(String s) {
        return s == null ? null : new BigInteger(s);
    }

    /**
     * Convert BigInteger to String
     */
    @Converter
    public String toString(BigInteger bi) {
        return bi == null ? null : bi.toString();
    }

}
