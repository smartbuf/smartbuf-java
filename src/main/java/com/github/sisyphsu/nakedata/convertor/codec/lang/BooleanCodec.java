package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;

/**
 * Boolean's codec
 *
 * @author sulin
 * @since 2019-05-13 18:12:00
 */
public class BooleanCodec extends Codec {

    /**
     * Convert Integer to Boolean
     * 0 => false
     * !0 => true
     *
     * @param l Integer
     * @return Boolean
     */
    @Converter
    public Boolean toBoolean(Long l) {
        return l == null ? null : l != 0;
    }

    /**
     * Convert Boolean to Long
     *
     * @param b Boolean
     * @return Long
     */
    @Converter
    public Long toLong(Boolean b) {
        return b == null ? null : (b ? 1L : 0L);
    }

    /**
     * Convert String to Boolean
     *
     * @param s String
     * @return Boolean
     */
    @Converter
    public Boolean toBoolean(String s) {
        if (s == null)
            return null;
        return Boolean.valueOf(s);
    }

    /**
     * Convert Boolean to String
     *
     * @param b Boolean
     * @return String
     */
    @Converter
    public String toString(Boolean b) {
        return b == null ? null : b.toString();
    }

    /**
     * Convert boolean[] to Boolean[]
     *
     * @param arr boolean[]
     * @return Boolean[]
     */
    @Converter
    public Boolean[] convert(boolean[] arr) {
        if (arr == null) {
            return null;
        }
        Boolean[] result = new Boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    /**
     * Convert Boolean[] to boolean[]
     *
     * @param arr Boolean[]
     * @return boolean[]
     */
    @Converter
    public boolean[] convert(Boolean[] arr) {
        if (arr == null) {
            return null;
        }
        boolean[] result = new boolean[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i]; // could NPE
        }
        return result;
    }

}
