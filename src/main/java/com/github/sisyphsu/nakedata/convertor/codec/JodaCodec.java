package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;
import org.joda.time.*;

/**
 * Codec for `org.joda.time` package.
 *
 * @author sulin
 * @since 2019-07-26 11:56:20
 */
public class JodaCodec extends Codec {

    /**
     * Convert Long(timestamp) to Duration
     */
    @Converter
    public Duration toDuration(Long ms) {
        return new Duration(ms);
    }

    /**
     * Convert Instant to Long(timestamp)
     */
    @Converter
    public Long toLong(Instant instant) {
        return instant.getMillis();
    }

    /**
     * Convert LocalDateTime to LocalDate
     */
    @Converter
    public LocalDate toLocalDate(LocalDateTime ldt) {
        return ldt.toLocalDate();
    }

    /**
     * Convert ZonedDateTime to LocalDateTime
     */
    @Converter
    public LocalDateTime toLocalDateTime() {
        return null;
    }

    /**
     * Convert OffsetTime to LocalTime
     */
    @Converter
    public LocalTime toLocalTime() {
        return null;
    }

    /**
     * Convert Integer to Period
     */
    @Converter
    public Period toPeriod(Long ms) {
        return new Period(ms);
    }

    /**
     * Convert Period to Integer
     */
    @Converter
    public Long toInteger(Period period) {
        return null;
    }

}
