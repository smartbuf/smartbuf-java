package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.Codec;
import com.github.sisyphsu.smartbuf.converter.Converter;

import java.text.StringCharacterIterator;

/**
 * String's codec
 *
 * @author sulin
 * @since 2019-05-13 18:14:25
 */
public final class StringCodec extends Codec {

    @Converter
    public String toString(StringBuffer sb) {
        return sb.toString();
    }

    @Converter
    public StringBuffer toStringBuffer(String s) {
        return new StringBuffer(s);
    }

    @Converter
    public String toString(StringBuilder sb) {
        return sb.toString();
    }

    @Converter
    public StringBuilder toStringBuilder(String s) {
        return new StringBuilder(s);
    }

    @Converter(distance = 101)
    public Character toCharacter(String s) {
        if (s.length() != 1) {
            throw new UnsupportedOperationException("Convert string to char failed, string.length > 0: " + s);
        }
        return s.charAt(0);
    }

    @Converter(distance = 101)
    public String toString(Character c) {
        return c.toString();
    }

    @Converter
    public String toString(char[] chars) {
        return new String(chars);
    }

    @Converter
    public char[] toCharArray(String s) {
        return s.toCharArray();
    }

    @Converter
    public String toString(byte[] bytes) {
        return new String(bytes);
    }

    @Converter
    public byte[] toByteArray(String s) {
        return s.getBytes();
    }

    @Converter
    public String toString(StringCharacterIterator sc) {
        StringBuilder sb = new StringBuilder();
        char c = sc.first();
        while (c != StringCharacterIterator.DONE) {
            sb.append(c);
            c = sc.next();
        }
        return sb.toString();
    }

    @Converter
    public StringCharacterIterator toStringCharacterIterator(String s) {
        return new StringCharacterIterator(s);
    }

}
