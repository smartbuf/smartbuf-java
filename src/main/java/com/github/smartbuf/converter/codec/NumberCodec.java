package com.github.smartbuf.converter.codec;

import com.github.smartbuf.converter.Codec;
import com.github.smartbuf.converter.Converter;

import java.text.DecimalFormat;

/**
 * Codec for Number and its descendants, include Boolean
 *
 * @author sulin
 * @since 2019-05-13 18:14:18
 */
public final class NumberCodec extends Codec {

    private static final DecimalFormat F_F = new DecimalFormat("0.#######");
    private static final DecimalFormat D_F = new DecimalFormat("0.################");

    @Converter
    public Long toLong(String s) {
        return Long.parseLong(s);
    }

    @Converter
    public String toString(Long l) {
        return l.toString();
    }

    @Converter
    public Short toShort(Long l) {
        return l.shortValue();
    }

    @Converter
    public Long toLong(Short s) {
        return s.longValue();
    }

    @Converter
    public Integer toInteger(Long l) {
        return l.intValue();
    }

    @Converter
    public Long toLong(Integer i) {
        return i.longValue();
    }

    @Converter
    public Float toFloat(Double d) {
        return d.floatValue();
    }

    @Converter
    public Float toFloat(String s) {
        return Float.parseFloat(s);
    }

    @Converter
    public Double toDouble(Float f) {
        return f.doubleValue();
    }

    @Converter
    public String toString(Float f) {
        return F_F.format(f);
    }

    @Converter
    public Double toDouble(String s) {
        return Double.parseDouble(s);
    }

    @Converter
    public Double toDouble(Long l) {
        return l.doubleValue();
    }

    @Converter
    public Long toLong(Double d) {
        return d.longValue();
    }

    @Converter
    public String toString(Double d) {
        return D_F.format(d);
    }

    @Converter
    public Byte toByte(Long l) {
        return l.byteValue();
    }

    @Converter
    public Long toLong(Byte b) {
        return b.longValue();
    }

    @Converter
    public Boolean toBoolean(Long l) {
        // Convert Integer to Boolean; 0 => false; !0 => true
        return l != 0;
    }

    @Converter
    public Long toLong(Boolean b) {
        return b ? 1L : 0L;
    }

    @Converter
    public Boolean toBoolean(String s) {
        return Boolean.valueOf(s);
    }

    @Converter
    public String toString(Boolean b) {
        return b.toString();
    }

    @Converter
    public Character toCharacter(Long i) {
        return (char) i.intValue();
    }

    @Converter
    public Long toInteger(Character c) {
        return (long) (int) c;
    }

}
