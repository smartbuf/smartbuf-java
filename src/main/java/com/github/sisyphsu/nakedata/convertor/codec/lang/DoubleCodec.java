package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

/**
 * Double's codec
 *
 * @author sulin
 * @since 2019-05-13 18:14:06
 */
public class DoubleCodec extends Codec {

    /**
     * Convert Float to Double
     *
     * @param f Float
     * @return Double
     */
    @Converter
    public Double toDouble(Float f) {
        return f == null ? null : f.doubleValue();
    }

    /**
     * Convert String to Double
     *
     * @param s String
     * @return Double
     */
    @Converter
    public Double toDouble(String s) {
        if (s == null)
            return null;
        return Double.parseDouble(s);
    }

    /**
     * Convert Long to Double
     *
     * @param l Long
     * @return Double
     */
    @Converter
    public Double toDouble(Long l) {
        return l == null ? null : l.doubleValue();
    }

    /**
     * Convert double[] to Double[]
     *
     * @param arr double[]
     * @return Double[]
     */
    @Converter
    public Double[] convert(double[] arr) {
        if (arr == null) {
            return null;
        }
        Double[] result = new Double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    /**
     * Convert Double[] to double[]
     *
     * @param arr Double[]
     * @return double[]
     */
    @Converter
    public double[] convert(Double[] arr) {
        if (arr == null) {
            return null;
        }
        double[] result = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

}
