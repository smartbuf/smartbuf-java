package com.github.sisyphsu.nakedata.convertor.codec.atomic;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Convert everything to AtomicIntegerArray if can
 *
 * @author sulin
 * @since 2019-05-13 18:20:57
 */
public class AtomicIntegerArrayCodec extends Codec {

    /**
     * Convert int[] to AtomicIntegerArray
     *
     * @param arr int[]
     * @return AtomicIntegerArray
     */
    @Converter
    public AtomicIntegerArray toAtomicIntegerArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        return new AtomicIntegerArray(arr);
    }

}
