package com.github.sisyphsu.nakedata.convertor.codec;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author sulin
 * @since 2019-08-06 14:49:44
 */
class DateTimeCodecTest {

    @Test
    public void test() {
        DateTimeFormatter parser = DateTimeFormatter.ofPattern("[MM/dd/yyyy'T'HH:mm][yyyy-M-d'T'HH:mm:ss]");

        System.out.println(LocalDate.parse("10/16/2016", parser));
        System.out.println(LocalDate.parse("10/16/2016 11:20", parser));
        System.out.println(LocalDate.parse("2016-10-16T10:20:30", parser));
    }

}