package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;

/**
 * StringBuffer's codec
 *
 * @author sulin
 * @since 2019-05-13 18:26:27
 */
public class StringBufferCodec extends Codec {

    /**
     * Convert String to StringBuffer
     *
     * @param s String
     * @return StringBuffer
     */
    @Converter
    public StringBuffer toStringBuffer(String s) {
        return s == null ? null : new StringBuffer(s);
    }

    /**
     * Convert StringBuffer to String
     *
     * @param sb StringBuffer
     * @return String
     */
    @Converter
    public String toString(StringBuffer sb) {
        return sb == null ? null : sb.toString();
    }

}
