package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

/**
 * String's codec
 *
 * @author sulin
 * @since 2019-05-13 18:14:25
 */
public class StringCodec extends Codec {

    /**
     * Convert char[] to String
     *
     * @param chars char[]
     * @return String
     */
    @Converter
    public String toString(char[] chars) {
        return chars == null ? null : new String(chars);
    }

    /**
     * Convert String to char[]
     *
     * @param s String
     * @return char[]
     */
    @Converter
    public char[] toCharArray(String s) {
        return s == null ? null : s.toCharArray();
    }

    /**
     * Convert String to byte[]
     *
     * @param s String
     * @return byte[]
     */
    @Converter
    public byte[] toByteArray(String s) {
        return s == null ? null : s.getBytes();
    }

}
