package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;

import java.text.SimpleDateFormat;

/**
 * SimpleDateFormat's codec
 *
 * @author sulin
 * @since 2019-05-13 18:29:02
 */
public class TextCodec extends Codec {

    /**
     * Convert String to SimpleDateFormat
     *
     * @param s String
     * @return SimpleDateFormat
     */
    @Converter
    public SimpleDateFormat toSimpleDateFormat(String s) {
        if (s == null)
            return null;

        return new SimpleDateFormat(s);
    }

    /**
     * Convert SimpleDateFormat to String
     *
     * @param format SimpleDateFormat
     * @return Pattern String
     */
    @Converter
    public String toString(SimpleDateFormat format) {
        if (format == null)
            return null;

        return format.toPattern();
    }

}
