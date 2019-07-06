package com.github.sisyphsu.nakedata.convertor.codec.atomic;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.concurrent.atomic.AtomicLongArray;


/**
 * Convert everything to AtomicLongArray if can
 *
 * @author sulin
 * @since 2019-05-13 18:21:16
 */
public class AtomicLongArrayCodec extends Codec {

    /**
     * Convert long[] to AtomicLongArray
     *
     * @param arr long[]
     * @return AtomicLongArray
     */
    public AtomicLongArray toAtomicLongArray(long[] arr) {
        if (arr == null)
            return null;

        return new AtomicLongArray(arr);
    }

}
