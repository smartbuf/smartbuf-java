package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.OptionalLong;

/**
 * OptionalLong's Codec
 *
 * @author sulin
 * @since 2019-05-13 18:15:53
 */
public class OptionalLongCodec extends Codec {

    /**
     * Convert Long to OptionalLong
     *
     * @param l Long
     * @return OptionalLong
     */
    public OptionalLong toOptionalLong(Long l) {
        if (l == null)
            return OptionalLong.empty();

        return OptionalLong.of(l);
    }

    /**
     * Convert OptionalLong to Long
     *
     * @param ol OptionalLong
     * @return Long
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public Long toLong(OptionalLong ol) {
        if (ol.isPresent())
            return ol.getAsLong();

        return null;
    }

}
