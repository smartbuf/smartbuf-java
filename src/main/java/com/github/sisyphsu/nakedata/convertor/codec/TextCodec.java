package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;

import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;

/**
 * Codec for java.text package.
 *
 * @author sulin
 * @since 2019-05-13 18:29:02
 */
public class TextCodec extends Codec {

    /**
     * Convert String to SimpleDateFormat
     */
    @Converter
    public SimpleDateFormat toSimpleDateFormat(String s) {
        return new SimpleDateFormat(s);
    }

    /**
     * Convert SimpleDateFormat to String
     */
    @Converter
    public String toString(SimpleDateFormat format) {
        return format.toPattern();
    }

    /**
     * Convert String to StringCharacterIterator
     */
    @Converter
    public StringCharacterIterator toStringCharacterIterator(String s) {
        return new StringCharacterIterator(s);
    }

    /**
     * Convert StringCharacterIterator to String
     */
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

}
