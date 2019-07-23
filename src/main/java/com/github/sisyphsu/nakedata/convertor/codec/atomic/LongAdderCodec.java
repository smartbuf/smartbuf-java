package com.github.sisyphsu.nakedata.convertor.codec.atomic;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.concurrent.atomic.LongAdder;

/**
 * Convert everything to LongAdder if can
 *
 * @author sulin
 * @since 2019-05-13 18:20:17
 */
public class LongAdderCodec extends Codec {

    /**
     * Convert Long to LongAdder
     *
     * @param l Long
     * @return LongAdder
     */
    @Converter
    public LongAdder toLongAdder(Long l) {
        if (l == null) {
            return null;
        }
        LongAdder adder = new LongAdder();
        adder.add(l);
        return adder;
    }

}
