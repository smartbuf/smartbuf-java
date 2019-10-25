package com.github.sisyphsu.datatube.convertor.codec;

import com.github.sisyphsu.datatube.convertor.Codec;
import com.github.sisyphsu.datatube.convertor.Converter;

/**
 * Codec for primary data type
 *
 * @author sulin
 * @since 2019-08-05 19:44:37
 */
public final class PrimaryCodec extends Codec {

    @Converter
    public boolean toBoolean(Boolean b) {
        return b;
    }

    @Converter
    public Boolean toBoolean(boolean b) {
        return b;
    }

    @Converter
    public byte toByte(Byte b) {
        return b;
    }

    @Converter
    public Byte toByte(byte b) {
        return b;
    }

    @Converter
    public short toShort(Short s) {
        return s;
    }

    @Converter
    public Short toShort(short s) {
        return s;
    }

    @Converter
    public int toInt(Integer i) {
        return i;
    }

    @Converter
    public Integer toInteger(int i) {
        return i;
    }

    @Converter
    public long toLong(Long l) {
        return l;
    }

    @Converter
    public Long toLong(long l) {
        return l;
    }

    @Converter
    public float toFloat(Float f) {
        return f;
    }

    @Converter
    public Float toFloat(float f) {
        return f;
    }

    @Converter
    public double toDouble(Double d) {
        return d;
    }

    @Converter
    public Double toDouble(double d) {
        return d;
    }

    @Converter
    public char toChar(Character c) {
        return c;
    }

    @Converter
    public Character toCharacter(char c) {
        return c;
    }

}
