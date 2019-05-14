package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.time.LocalDate;

/**
 * {@link LocalDate}与{@link Long}的数据转换适配器
 *
 * @author sulin
 * @since 2019-05-10 10:44:06
 */
public class LocalDateCodec extends Codec<LocalDate> {

    protected Long toTargetNotNull(LocalDate localDate) {
        return localDate.toEpochDay();
    }

    protected LocalDate toSourceNotNull(Long aLong) {
        return LocalDate.ofEpochDay(aLong);
    }

}
