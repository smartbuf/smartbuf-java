package com.github.sisyphsu.nakedata.convertor.codec.xml;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;

/**
 * XMLGregorianCalendar's codec
 *
 * @author sulin
 * @since 2019-05-13 18:25:17
 */
public class XMLGregorianCalendarCodec extends Codec {

    /**
     * Convert XMLGregorianCalendar to Calendar
     *
     * @param calendar XMLGregorianCalendar
     * @return Calendar
     */
    @Converter
    public Calendar toCalendar(XMLGregorianCalendar calendar) {
        if (calendar == null)
            return null;

        return calendar.toGregorianCalendar();
    }

}
