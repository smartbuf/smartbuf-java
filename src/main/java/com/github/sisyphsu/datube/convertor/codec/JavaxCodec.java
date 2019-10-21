package com.github.sisyphsu.datube.convertor.codec;

import com.github.sisyphsu.datube.convertor.Codec;
import com.github.sisyphsu.datube.convertor.Converter;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;

/**
 * Codec for javax.xml package.
 *
 * @author sulin
 * @since 2019-05-13 18:25:17
 */
public final class JavaxCodec extends Codec {

    /**
     * Convert XMLGregorianCalendar to Calendar
     */
    @Converter
    public Calendar toCalendar(XMLGregorianCalendar calendar) {
        return calendar.toGregorianCalendar();
    }

}
