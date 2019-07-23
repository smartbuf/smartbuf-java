package com.github.sisyphsu.nakedata.convertor.codec.sql;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.sql.Timestamp;

/**
 * Timestamp's codec
 *
 * @author sulin
 * @since 2019-05-13 18:07:34
 */
public class TimestampCodec extends Codec {

    /**
     * Convert Long to Timestamp
     *
     * @param l Long
     * @return Timestamp
     */
    @Converter
    public Timestamp toTimestamp(Long l) {
        if (l == null)
            return null;

        return new Timestamp(l);
    }

    /**
     * Convert Timestamp to Long
     *
     * @param t Timestamp
     * @return Long
     */
    @Converter
    public Long toLong(Timestamp t) {
        if (t == null)
            return null;

        return t.getTime();
    }

}
