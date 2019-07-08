package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

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
    public Float toFloat(Double d) {
        return d == null ? null : d.floatValue();
    }

    /**
     * Convert float[] to Float[]
     *
     * @param arr float[]
     * @return Float[]
     */
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
