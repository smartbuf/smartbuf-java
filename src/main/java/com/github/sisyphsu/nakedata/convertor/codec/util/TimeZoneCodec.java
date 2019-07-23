package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.TimeZone;

/**
 * TimeZone's codec
 *
 * @author sulin
 * @since 2019-05-13 20:24:23
 */
public class TimeZoneCodec extends Codec {

    /**
     * Convert String to TimeZone
     *
     * @param s String
     * @return TimeZone
     */
    @Converter
    public TimeZone toTimeZone(String s) {
        if (s == null)
            return null;
        
        return TimeZone.getTimeZone(s);
    }

    /**
     * Convert TimeZone to String
     *
     * @param tz TimeZone
     * @return String
     */
    @Converter
    public String toString(TimeZone tz) {
        if (tz == null)
            return null;

        return tz.getID();
    }

}
