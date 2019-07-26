package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;

/**
 * String's codec
 *
 * @author sulin
 * @since 2019-05-13 18:14:25
 */
public class StringCodec extends Codec {

    /**
     * Convert String to Character
     */
    @Converter
    public Character toCharacter(String s) {
        if (s.length() != 1)
            throw new UnsupportedOperationException("Convert String to Character failed: " + s);
        return s.charAt(0);
    }

    /**
     * Convert Character to String
     */
    @Converter
    public String toString(Character c) {
        return c.toString();
    }

    /**
     * Convert Integer to Character
     */
    @Converter
    public Character toCharacter(Integer i) {
        return (char) i.intValue();
    }

    /**
     * Convert Character to Integer
     */
    @Converter
    public Integer toInteger(Character c) {
        return (int) c;
    }

    /**
     * Convert String to StringBuilder
     */
    @Converter
    public StringBuilder toStringBuilder(String s) {
        return new StringBuilder(s);
    }

    /**
     * Convert StringBuilder to String
     */
    @Converter
    public String toString(StringBuilder sb) {
        return sb.toString();
    }

    /**
     * Convert char[] to String
     */
    @Converter
    public String toString(char[] chars) {
        return new String(chars);
    }

    /**
     * Convert String to char[]
     */
    @Converter
    public char[] toCharArray(String s) {
        return s.toCharArray();
    }

    /**
     * Convert String to byte[]
     */
    @Converter
    public byte[] toByteArray(String s) {
        return s.getBytes();
    }

    /**
     * Convert String to StringBuffer
     */
    @Converter
    public StringBuffer toStringBuffer(String s) {
        return new StringBuffer(s);
    }

    /**
     * Convert StringBuffer to String
     */
    @Converter
    public String toString(StringBuffer sb) {
        return sb.toString();
    }

}
