package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.time.OffsetDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/**
 * {@link OffsetDateTime}与{@link String}的数据转换适配器
 *
 * @author sulin
 * @since 2019-05-10 10:45:30
 */
public class OffsetDateTimeCodec extends Codec<OffsetDateTime> {

    protected String toTargetNotNull(OffsetDateTime offsetDateTime) {
        return offsetDateTime.format(ISO_LOCAL_DATE);
    }

    protected OffsetDateTime toSourceNotNull(String s) {
        return OffsetDateTime.parse(s, ISO_LOCAL_DATE);
    }

}
