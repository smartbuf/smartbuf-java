package com.github.sisyphsu.nakedata.convertor.codec.time.joda;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;


/**
 * LocalDate's codec
 *
 * @author sulin
 * @since 2019-05-10 10:44:06
 */
public class LocalDateCodec extends Codec {

    /**
     * Convert LocalDateTime to LocalDate
     *
     * @param ldt LocalDateTime
     * @return LocalDate
     */
    public LocalDate toLocalDate(LocalDateTime ldt) {
        return ldt == null ? null : ldt.toLocalDate();
    }

}
