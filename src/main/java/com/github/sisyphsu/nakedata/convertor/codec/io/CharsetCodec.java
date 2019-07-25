package com.github.sisyphsu.nakedata.convertor.codec.io;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.nio.charset.Charset;

/**
 * Charset's codec
 *
 * @author sulin
 * @since 2019-05-13 18:58:43
 */
public class CharsetCodec extends Codec {

    /**
     * Convert String to Charset
     */
    @Converter
    public Charset toCharset(String s) {
        return s == null ? null : Charset.forName(s);
    }

    /**
     * Convert Charset to String
     */
    @Converter
    public String toString(Charset c) {
        return c == null ? null : c.name();
    }

}
