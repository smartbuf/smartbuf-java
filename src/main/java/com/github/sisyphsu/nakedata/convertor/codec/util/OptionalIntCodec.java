package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.OptionalInt;

/**
 * OptionalInt's codec
 *
 * @author sulin
 * @since 2019-05-13 18:15:34
 */
public class OptionalIntCodec extends Codec {

    /**
     * Convert Integer to OptionalInt
     *
     * @param i Integer
     * @return OptionalInt
     */
    public OptionalInt toOptionalInt(Integer i) {
        if (i == null)
            return OptionalInt.empty();
        return OptionalInt.of(i);
    }

    /**
     * Convert OptionalInt to Integer
     *
     * @param oi OptionalInt
     * @return Integer
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public Integer toInteger(OptionalInt oi) {
        if (oi.isPresent())
            return oi.getAsInt();

        return null;
    }

}
