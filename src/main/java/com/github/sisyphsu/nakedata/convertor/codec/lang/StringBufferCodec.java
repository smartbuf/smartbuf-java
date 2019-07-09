package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

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
    public StringBuffer toStringBuffer(String s) {
        return s == null ? null : new StringBuffer(s);
    }

    /**
     * Convert StringBuffer to String
     *
     * @param sb StringBuffer
     * @return String
     */
    public String toString(StringBuffer sb) {
        return sb == null ? null : sb.toString();
    }

}
