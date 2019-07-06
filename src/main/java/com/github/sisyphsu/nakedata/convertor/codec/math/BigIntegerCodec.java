package com.github.sisyphsu.nakedata.convertor.codec.math;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.math.BigInteger;

/**
 * BigInteger's codec
 *
 * @author sulin
 * @since 2019-05-13 18:16:52
 */
public class BigIntegerCodec extends Codec {

    /**
     * Convert String to BigInteger
     *
     * @param s String
     * @return BigInteger
     */
    public BigInteger toBigInteger(String s) {
        if (s == null)
            return null;

        return new BigInteger(s);
    }

    /**
     * Convert BigInteger to String
     *
     * @param bi BigInteger
     * @return String
     */
    public String toString(BigInteger bi) {
        if (bi == null)
            return null;

        return bi.toString();
    }

}
