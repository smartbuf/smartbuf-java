package com.github.sisyphsu.nakedata.convertor.codec.io;

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
     *
     * @param s String
     * @return Charset
     */
    public Charset toCharset(String s) {
        if (s == null)
            return null;

        return Charset.forName(s);
    }

    /**
     * Convert Charset to String
     *
     * @param c Charset
     * @return String
     */
    public String toString(Charset c) {
        if (c == null)
            return null;

        return c.name();
    }

}
