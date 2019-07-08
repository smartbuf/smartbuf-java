package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.Currency;

/**
 * Currency's codec
 *
 * @author sulin
 * @since 2019-05-13 20:23:40
 */
public class CurrencyCodec extends Codec {

    /**
     * Convert String to Currency
     *
     * @param s String
     * @return Currency
     */
    public Currency toCurrency(String s) {
        if (s == null)
            return null;

        return Currency.getInstance(s);
    }

    /**
     * Convert Currency to String
     *
     * @param c Currency
     * @return String
     */
    public String toString(Currency c) {
        if (c == null)
            return null;
        return c.getCurrencyCode();
    }

}
