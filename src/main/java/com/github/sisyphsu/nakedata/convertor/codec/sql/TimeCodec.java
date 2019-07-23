package com.github.sisyphsu.nakedata.convertor.codec.sql;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.sql.Time;

/**
 * sql.Time's codec
 *
 * @author sulin
 * @since 2019-05-13 18:06:10
 */
public class TimeCodec extends Codec {

    /**
     * Convert String to Time
     *
     * @param s String
     * @return Time
     */
    @Converter
    public Time toTime(String s) {
        if (s == null)
            return null;

        return Time.valueOf(s);
    }

    /**
     * Convert Time to String
     *
     * @param time Time
     * @return String
     */
    @Converter
    public String toString(Time time) {
        if (time == null)
            return null;

        return time.toString();
    }

}
