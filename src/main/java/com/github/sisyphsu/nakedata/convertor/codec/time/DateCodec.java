package com.github.sisyphsu.nakedata.convertor.codec.time;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.Date;

/**
 * Date's codec, don't support TimeZone
 *
 * @author sulin
 * @since 2019-05-13 18:05:26
 */
public class DateCodec extends Codec {

    /**
     * Convert Long to Date
     *
     * @param l Long
     * @return Date
     */
    @Converter
    public Date toDate(Long l) {
        if (l == null)
            return null;
        return new Date(l);
    }

    /**
     * Convert Date to Long(Timestamp)
     *
     * @param d Date
     * @return Long
     */
    @Converter
    public Long toTimestamp(Date d) {
        if (d == null)
            return null;
        return d.getTime();
    }

}
