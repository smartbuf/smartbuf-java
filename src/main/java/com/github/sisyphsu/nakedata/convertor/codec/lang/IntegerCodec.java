package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

/**
 * Integer's codec
 *
 * @author sulin
 * @since 2019-05-13 18:12:41
 */
public class IntegerCodec extends Codec {

    /**
     * Convert Long to Integer
     *
     * @param l Long
     * @return Integer
     */
    @Converter
    public Integer toInteger(Long l) {
        return l == null ? null : l.intValue();
    }

    /**
     * Convert Integer to Long
     *
     * @param i Integer
     * @return Long
     */
    @Converter
    public Long toLong(Integer i) {
        return i == null ? null : i.longValue();
    }

    /**
     * Convert Integer[] to int[]
     *
     * @param arr int[]
     * @return Integer[]
     */
    @Converter
    public Integer[] convert(int[] arr) {
        if (arr == null) {
            return null;
        }
        Integer[] result = new Integer[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    /**
     * Convert Integer[] to int[]
     *
     * @param arr Integer[]
     * @return int[]
     */
    @Converter
    public int[] convert(Integer[] arr) {
        if (arr == null) {
            return null;
        }
        int[] result = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

}
