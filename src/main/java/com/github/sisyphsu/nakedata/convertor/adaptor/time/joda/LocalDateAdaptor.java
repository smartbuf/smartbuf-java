package com.github.sisyphsu.nakedata.convertor.adaptor.time.joda;

import com.github.sisyphsu.nakedata.convertor.adaptor.Adaptor;

import java.time.LocalDate;

/**
 * {@link LocalDate}与{@link Long}的数据转换适配器
 *
 * @author sulin
 * @since 2019-05-10 10:44:06
 */
public class LocalDateAdaptor extends Adaptor<LocalDate, Long> {

    @Override
    protected Long toTargetNotNull(LocalDate localDate) {
        return localDate.toEpochDay();
    }

    @Override
    protected LocalDate toSourceNotNull(Long aLong) {
        return LocalDate.ofEpochDay(aLong);
    }

}
