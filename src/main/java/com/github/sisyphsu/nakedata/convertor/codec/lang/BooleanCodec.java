package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

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
     * @param i Integer
     * @return Boolean
     */
    public Boolean toBoolean(Integer i) {
        return i == null ? null : i != 0;
    }

    /**
     * Convert String to Boolean
     *
     * @param s String
     * @return Boolean
     */
    public Boolean toBoolean(String s) {
        if (s == null)
            return null;
        return Boolean.valueOf(s);
    }

    /**
     * Convert boolean[] to Boolean[]
     *
     * @param arr boolean[]
     * @return Boolean[]
     */
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
