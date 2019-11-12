package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.Converter;
import com.github.sisyphsu.smartbuf.converter.Codec;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Codec for java.math package, include BigDecimal and BigInteger.
 *
 * @author sulin
 * @since 2019-07-25 12:17:16
 */
public final class MathCodec extends Codec {

    @Converter
    public BigDecimal toBigDecimal(String s) {
        return new BigDecimal(s);
    }

    @Converter
    public String toString(BigDecimal bd) {
        return bd.toPlainString();
    }

    @Converter
    public BigInteger toBigInteger(String s) {
        return new BigInteger(s);
    }

    @Converter
    public String toString(BigInteger bi) {
        return bi.toString();
    }

}
