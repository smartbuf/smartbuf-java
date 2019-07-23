package com.github.sisyphsu.nakedata.convertor.codec.sql;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.sql.Date;

/**
 * Date's codec
 *
 * @author sulin
 * @since 2019-05-13 18:06:50
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
     * Convert Date into java.util.Date, to use its codec
     *
     * @param date Date
     * @return java.util.Date
     */
    @Converter
    public Long toLong(Date date) {
        return date == null ? null : date.getTime();
    }

}
