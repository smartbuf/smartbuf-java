package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;

/**
 * Codec for Number and its descendants, include Boolean
 *
 * @author sulin
 * @since 2019-05-13 18:14:18
 */
public class NumberCodec extends Codec {

    /**
     * Convert String to Long
     */
    @Converter
    public Long toLong(String s) {
        return Long.parseLong(s);
    }

    /**
     * Convert Long to String
     */
    @Converter
    public String toString(Long l) {
        return l.toString();
    }

    /**
     * Convert Long to Short
     */
    @Converter
    public Short toShort(Long l) {
        return l.shortValue();
    }

    /**
     * Convert Short to Long
     */
    @Converter
    public Long toLong(Short s) {
        return s.longValue();
    }

    /**
     * Convert Long to Integer
     */
    @Converter
    public Integer toInteger(Long l) {
        return l.intValue();
    }

    /**
     * Convert Integer to Long
     */
    @Converter
    public Long toLong(Integer i) {
        return i.longValue();
    }

    /**
     * Convert Double to Float
     */
    @Converter
    public Float toFloat(Double d) {
        return d.floatValue();
    }

    /**
     * Convert Float to Double
     */
    @Converter
    public Double toDouble(Float f) {
        return f.doubleValue();
    }

    /**
     * Convert String to Double
     */
    @Converter
    public Double toDouble(String s) {
        return Double.parseDouble(s);
    }

    /**
     * Convert Long to Double
     */
    @Converter
    public Double toDouble(Long l) {
        return l.doubleValue();
    }

    /**
     * Convert Long to Byte
     */
    @Converter
    public Byte toByte(Long l) {
        return l.byteValue();
    }

    /**
     * Convert Byte to Long
     */
    @Converter
    public Long toLong(Byte b) {
        return b.longValue();
    }

    /**
     * Convert Integer to Boolean
     * 0 => false
     * !0 => true
     */
    @Converter
    public Boolean toBoolean(Long l) {
        return l != 0;
    }

    /**
     * Convert Boolean to Long
     */
    @Converter
    public Long toLong(Boolean b) {
        return b ? 1L : 0L;
    }

    /**
     * Convert String to Boolean
     */
    @Converter
    public Boolean toBoolean(String s) {
        return Boolean.valueOf(s);
    }

    /**
     * Convert Boolean to String
     */
    @Converter
    public String toString(Boolean b) {
        return b.toString();
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

}
