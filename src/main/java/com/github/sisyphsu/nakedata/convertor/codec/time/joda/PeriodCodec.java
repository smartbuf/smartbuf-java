package com.github.sisyphsu.nakedata.convertor.codec.time.joda;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import org.joda.time.Period;

/**
 * Period's codec
 *
 * @author sulin
 * @since 2019-05-10 10:46:24
 */
public class PeriodCodec extends Codec {

    /**
     * Convert Integer to Period
     *
     * @param i Integer
     * @return Period
     */
    public Period toPeriod(Integer i) {
        if (i == null)
            return null;
        int year = i / 10000;
        int month = (i % 10000) / 100;
        int day = i % 100;
        return null;
    }

    /**
     * Convert Period to Integer
     *
     * @param period Period
     * @return Integer
     */
    public Integer toInteger(Period period) {
        if (period == null)
            return null;
        return period.getYears() * 10000 + period.getMonths() * 100 + period.getDays();
    }

}
