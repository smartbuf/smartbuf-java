package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

/**
 * Long's codec
 *
 * @author sulin
 * @since 2019-05-13 18:13:32
 */
public class LongCodec extends Codec {

    /**
     * Convert String to Long
     *
     * @param s String
     * @return Long
     */
    public Long toLong(String s) {
        if (s == null)
            return null;
        return Long.parseLong(s);
    }

    /**
     * Convert Long[] to long[]
     *
     * @param arr long[]
     * @return Long[]
     */
    public Long[] convert(long[] arr) {
        if (arr == null) {
            return null;
        }
        Long[] result = new Long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    /**
     * Convert Long[] to long[]
     *
     * @param arr Long[]
     * @return long[]
     */
    public long[] convert(Long[] arr) {
        if (arr == null) {
            return null;
        }
        long[] result = new long[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

}
