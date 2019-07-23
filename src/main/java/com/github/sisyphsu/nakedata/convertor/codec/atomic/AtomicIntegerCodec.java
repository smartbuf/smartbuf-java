package com.github.sisyphsu.nakedata.convertor.codec.atomic;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Convert everything to AtomicInteger if can.
 *
 * @author sulin
 * @since 2019-05-13 18:20:44
 */
public class AtomicIntegerCodec extends Codec {

    /**
     * Convert Integer to AtomicInteger
     *
     * @param i Integer
     * @return AtomicInteger
     */
    @Converter
    public AtomicInteger toAtomicInteger(Integer i) {
        if (i == null) {
            return null;
        }
        return new AtomicInteger(i);
    }

}
