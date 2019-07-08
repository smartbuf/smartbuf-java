package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

/**
 * ZonedDateTime's codec
 *
 * @author sulin
 * @since 2019-05-10 10:44:59
 */
public class ZonedDateTimeCodec extends Codec {

    /**
     * Convert ZonedDateTime to OffsetDateTime
     *
     * @param zdt ZonedDateTime
     * @return OffsetDateTime
     */
    public OffsetDateTime toOffsetDateTime(ZonedDateTime zdt) {
        return zdt == null ? null : zdt.toOffsetDateTime();
    }

    /**
     * Convert OffsetDateTime to ZonedDateTime
     *
     * @param odt OffsetDateTime
     * @return ZonedDateTime
     */
    public ZonedDateTime toZonedDateTime(OffsetDateTime odt) {
        return odt == null ? null : odt.toZonedDateTime();
    }

}