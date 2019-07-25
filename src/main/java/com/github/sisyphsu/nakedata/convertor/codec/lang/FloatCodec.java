package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;

/**
 * Float's codec
 *
 * @author sulin
 * @since 2019-05-13 18:13:41
 */
public class FloatCodec extends Codec {

    /**
     * Convert Double to Float
     *
     * @param d Double
     * @return Double
     */
    @Converter
    public Float toFloat(Double d) {
        return d == null ? null : d.floatValue();
    }

    /**
     * Convert float[] to Float[]
     *
     * @param arr float[]
     * @return Float[]
     */
    @Converter
    public Float[] convert(float[] arr) {
        if (arr == null) {
            return null;
        }
        Float[] result = new Float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    /**
     * Convert Float[] to float[]
     *
     * @param arr Float[]
     * @return float[]
     */
    @Converter
    public float[] convert(Float[] arr) {
        if (arr == null) {
            return null;
        }
        float[] result = new float[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

}
