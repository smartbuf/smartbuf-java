package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

/**
 * StringBuilder's codec
 *
 * @author sulin
 * @since 2019-05-13 18:26:19
 */
public class StringBuilderCodec extends Codec {

    /**
     * Convert String to StringBuilder
     *
     * @param s String
     * @return StringBuilder
     */
    public StringBuilder toStringBuilder(String s) {
        return s == null ? null : new StringBuilder(s);
    }

    /**
     * Convert StringBuilder to String
     *
     * @param sb StringBuilder
     * @return String
     */
    public String toString(StringBuilder sb) {
        return sb == null ? null : sb.toString();
    }

}
