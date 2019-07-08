package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.time.LocalDate;
import java.time.LocalDateTime;

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

    /**
     * Convert LocalDate to LocalDateTime
     *
     * @param localDate LocalDate
     * @return LocalDateTime
     */
    public LocalDateTime toLong(LocalDate localDate) {
        return localDate == null ? null : localDate.atStartOfDay();
    }

}
