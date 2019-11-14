package com.github.smartbuf.converter.codec;

import com.github.smartbuf.converter.Codec;
import com.github.smartbuf.converter.Converter;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.Calendar;

/**
 * Codec for javax.xml package.
 *
 * @author sulin
 * @since 2019-05-13 18:25:17
 */
public final class JavaxCodec extends Codec {

    @Converter
    public Calendar toCalendar(XMLGregorianCalendar calendar) {
        return calendar.toGregorianCalendar();
    }

}
