package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.time.*;

/**
 * LocalDateTime's codec
 *
 * @author sulin
 * @since 2019-05-10 10:43:53
 */
public class LocalDateTimeCodec extends Codec {

    private static final ZoneOffset DEFAULT = OffsetDateTime.now().getOffset();

    /**
     * Convert ZonedDateTime to LocalDateTime
     *
     * @param zdt ZonedDateTime
     * @return LocalDateTime
     */
    @Converter
    public LocalDateTime toLocalDateTime(OffsetDateTime zdt) {
        return zdt == null ? null : zdt.toLocalDateTime();
    }

    /**
     * Convert LocalDateTime to ZonedDateTime
     *
     * @param ldt LocalDateTime
     * @return ZonedDateTime
     */
    @Converter
    public OffsetDateTime toZonedDateTime(LocalDateTime ldt) {
        return ldt == null ? null : ldt.atOffset(DEFAULT);
    }

}
