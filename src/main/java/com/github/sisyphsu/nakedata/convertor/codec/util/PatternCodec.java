package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;

import java.util.regex.Pattern;

/**
 * Pattern's codec
 *
 * @author sulin
 * @since 2019-05-13 18:56:30
 */
public class PatternCodec extends Codec {

    /**
     * Convert String to Pattern
     *
     * @param s String
     * @return Pattern
     */
    @Converter
    public Pattern toPattern(String s) {
        if (s == null)
            return null;

        return Pattern.compile(s);
    }

    /**
     * Convert Pattern to String
     *
     * @param p Pattern
     * @return String
     */
    @Converter
    public String toString(Pattern p) {
        if (p == null)
            return null;

        return p.pattern();
    }

}
