package com.github.sisyphsu.nakedata.convertor.codec.math;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.math.BigDecimal;

/**
 * BigDecimal's codec
 *
 * @author sulin
 * @since 2019-05-13 18:16:40
 */
public class BigDecimalCodec extends Codec {

    /**
     * Convert String to BigDecimal
     *
     * @param s String
     * @return BigDecimal
     */
    @Converter
    public BigDecimal toBigDecimal(String s) {
        if (s == null)
            return null;

        return new BigDecimal(s);
    }

    /**
     * Convert BigDecimal to String
     *
     * @param bd BigDecimal
     * @return String
     */
    @Converter
    public String toString(BigDecimal bd) {
        if (bd == null)
            return null;

        return bd.toPlainString();
    }

}
