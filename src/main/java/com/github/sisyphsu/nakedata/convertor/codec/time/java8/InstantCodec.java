package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.time.Instant;

/**
 * Instant's codec
 *
 * @author sulin
 * @since 2019-05-10 10:46:44
 */
public class InstantCodec extends Codec {

    /**
     * Convert Instant to Long
     *
     * @param instant Instant
     * @return Long
     */
    public Long toLong(Instant instant) {
        return instant == null ? null : instant.toEpochMilli();
    }

    /**
     * Convert Long to Instant
     *
     * @param l Long
     * @return Instant
     */
    public Instant toInstant(Long l) {
        return l == null ? null : Instant.ofEpochMilli(l);
    }

}
