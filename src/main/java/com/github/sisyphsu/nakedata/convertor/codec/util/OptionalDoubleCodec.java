package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.OptionalDouble;

/**
 * OptionalDouble's codec
 *
 * @author sulin
 * @since 2019-05-13 18:15:26
 */
public class OptionalDoubleCodec extends Codec {

    /**
     * Convert Double to OptionalDouble
     *
     * @param d Double
     * @return OptionalDouble
     */
    public OptionalDouble toOptionalDouble(Double d) {
        if (d == null)
            return OptionalDouble.empty();

        return OptionalDouble.of(d);
    }

    /**
     * Convert OptionalDouble to Double
     *
     * @param od OptionalDouble
     * @return Double
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public Double toDouble(OptionalDouble od) {
        if (od.isPresent())
            return od.getAsDouble();
        return null;
    }

}
