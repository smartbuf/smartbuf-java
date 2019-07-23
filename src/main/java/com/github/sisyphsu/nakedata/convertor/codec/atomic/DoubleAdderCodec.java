package com.github.sisyphsu.nakedata.convertor.codec.atomic;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.concurrent.atomic.DoubleAdder;

/**
 * Convert everything to DoubleAdder if can
 *
 * @author sulin
 * @since 2019-05-13 18:20:06
 */
public class DoubleAdderCodec extends Codec {

    /**
     * Convert Double into DoubleAdder
     *
     * @param d Double
     * @return DoubleAdder
     */
    @Converter
    public DoubleAdder toDoubleAdder(Double d) {
        if (d == null) {
            return null;
        }
        DoubleAdder addr = new DoubleAdder();
        addr.add(d);
        return addr;
    }

}
