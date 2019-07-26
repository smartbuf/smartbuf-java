package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;

/**
 * Codec for javax.xml package.
 *
 * @author sulin
 * @since 2019-05-13 18:25:17
 */
public class XmlCodec extends Codec {

    /**
     * Convert XMLGregorianCalendar to Calendar
     */
    @Converter
    public Calendar toCalendar(XMLGregorianCalendar calendar) {
        return calendar.toGregorianCalendar();
    }

}
