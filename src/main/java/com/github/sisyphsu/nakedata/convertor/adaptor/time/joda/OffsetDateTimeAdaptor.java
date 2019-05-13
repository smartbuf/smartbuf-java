package com.github.sisyphsu.nakedata.convertor.adaptor.time.joda;

import com.github.sisyphsu.nakedata.convertor.adaptor.Adaptor;

import java.time.OffsetDateTime;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

/**
 * {@link OffsetDateTime}与{@link String}的数据转换适配器
 *
 * @author sulin
 * @since 2019-05-10 10:45:30
 */
public class OffsetDateTimeAdaptor extends Adaptor<OffsetDateTime, String> {

    @Override
    protected String toTargetNotNull(OffsetDateTime offsetDateTime) {
        return offsetDateTime.format(ISO_LOCAL_DATE);
    }

    @Override
    protected OffsetDateTime toSourceNotNull(String s) {
        return OffsetDateTime.parse(s, ISO_LOCAL_DATE);
    }

}
