package com.github.sisyphsu.nakedata.convertor.codec.time;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.Calendar;

/**
 * Calendar's codec
 *
 * @author sulin
 * @since 2019-05-13 18:05:38
 */
public class CalendarCodec extends Codec {

    /**
     * Convert Long to Calendar
     *
     * @param l Long
     * @return Calendar
     */
    public Calendar toCalendar(Long l) {
        if (l == null)
            return null;

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(l);
        return c;
    }

    /**
     * Convert Calendar to Long
     *
     * @param c Calendar
     * @return Long
     */
    public Long toLong(Calendar c) {
        if (c == null)
            return null;
        return c.getTimeInMillis();
    }

}
