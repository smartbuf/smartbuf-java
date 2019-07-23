package com.github.sisyphsu.nakedata.convertor.codec.atomic;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Convert everything to AtomicBoolean if could
 *
 * @author sulin
 * @since 2019-05-13 18:20:34
 */
public class AtomicBooleanCodec extends Codec {

    /**
     * Convert Boolean to AtomicBoolean
     *
     * @param b Boolean
     * @return AtomicBoolean
     */
    @Converter
    public AtomicBoolean convertBoolean(Boolean b) {
        if (b == null) {
            return null;
        }
        return new AtomicBoolean(b);
    }

}
