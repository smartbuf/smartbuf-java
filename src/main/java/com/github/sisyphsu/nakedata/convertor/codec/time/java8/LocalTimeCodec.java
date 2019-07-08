package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneOffset;

/**
 * LocalTime's codec
 *
 * @author sulin
 * @since 2019-05-10 10:44:44
 */
public class LocalTimeCodec extends Codec {

    private static final ZoneOffset DEFAULT = OffsetDateTime.now().getOffset();

    /**
     * Convert OffsetTime to LocalTime
     *
     * @param ot OffsetTime
     * @return LocalTime
     */
    public LocalTime toLocalTime(OffsetTime ot) {
        return ot == null ? null : ot.toLocalTime();
    }

    /**
     * Convert LocalTime to OffsetTime
     *
     * @param localTime LocalTime
     * @return OffsetTime
     */
    protected OffsetTime toTargetNotNull(LocalTime localTime) {
        return localTime == null ? null : localTime.atOffset(DEFAULT);
    }

}
