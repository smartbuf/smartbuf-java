package com.github.sisyphsu.nakedata.convertor.codec.atomic;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Convert everything to AtomicLong if can
 *
 * @author sulin
 * @since 2019-05-13 18:21:07
 */
public class AtomicLongCodec extends Codec {

    /**
     * Convert Long to AtomicLong
     *
     * @param l Long
     * @return AtomicLong
     */
    public AtomicLong toAtomicLong(Long l) {
        if (l == null)
            return null;

        return new AtomicLong(l);
    }

}
