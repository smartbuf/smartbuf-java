package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.time.OffsetDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/**
 * OffsetDateTime's codec
 *
 * @author sulin
 * @since 2019-05-10 10:45:30
 */
public class OffsetDateTimeCodec extends Codec {

    public String toString(OffsetDateTime offsetDateTime) {
        return offsetDateTime.format(ISO_LOCAL_DATE);
    }

    public OffsetDateTime toOffsetDateTime(String s) {
        return OffsetDateTime.parse(s, ISO_LOCAL_DATE);
    }

}
