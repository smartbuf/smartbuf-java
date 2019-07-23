package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.Locale;

/**
 * Locale's codec
 *
 * @author sulin
 * @since 2019-05-13 18:55:43
 */
public class LocaleCodec extends Codec {

    /**
     * Convert String to Locale
     *
     * @param s String
     * @return Locale
     */
    @Converter
    public Locale toLocale(String s) {
        if (s == null)
            return null;

        return Locale.forLanguageTag(s);
    }

    /**
     * Convert Locale to String
     *
     * @param locale Locale
     * @return String
     */
    @Converter
    public String toString(Locale locale) {
        if (locale == null)
            return null;

        return locale.toLanguageTag();
    }

}
