package com.github.sisyphsu.nakedata.convertor.adaptor.time;

import com.github.sisyphsu.nakedata.convertor.adaptor.Adaptor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * {@link ZonedDateTime}类型适配器
 *
 * @author sulin
 * @since 2019-05-10 10:44:59
 */
public class ZonedDateTimeAdaptor extends Adaptor<ZonedDateTime, String> {

    @Override
    protected String toTargetNotNull(ZonedDateTime zonedDateTime) {
        return zonedDateTime.format(DateTimeFormatter.BASIC_ISO_DATE);
    }

    @Override
    protected ZonedDateTime toSourceNotNull(String s) {
        return ZonedDateTime.parse(s, DateTimeFormatter.BASIC_ISO_DATE);
    }

}