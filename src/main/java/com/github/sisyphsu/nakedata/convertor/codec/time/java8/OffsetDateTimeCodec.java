package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * OffsetDateTime's codec
 *
 * @author sulin
 * @since 2019-05-10 10:45:30
 */
public class OffsetDateTimeCodec extends Codec {

    private static DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    /**
     * Convert String to OffsetDateTime
     *
     * @param s String
     * @return OffsetDateTime
     */
    @Converter
    public OffsetDateTime toOffsetDateTime(String s) {
        return s == null ? null : OffsetDateTime.parse(s, FORMATTER);
    }

    /**
     * Convert OffsetDateTime to String
     *
     * @param odt OffsetDateTime
     * @return String
     */
    @Converter
    public String toString(OffsetDateTime odt) {
        return odt == null ? null : odt.format(FORMATTER);
    }

}
