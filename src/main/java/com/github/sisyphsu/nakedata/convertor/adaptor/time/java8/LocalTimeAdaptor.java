package com.github.sisyphsu.nakedata.convertor.adaptor.time.java8;

import com.github.sisyphsu.nakedata.convertor.adaptor.Adaptor;

import java.time.LocalTime;

/**
 * {@link LocalTime}与{@link Long}的类型转换适配器
 *
 * @author sulin
 * @since 2019-05-10 10:44:44
 */
public class LocalTimeAdaptor extends Adaptor<LocalTime, Long> {

    @Override
    protected Long toTargetNotNull(LocalTime localTime) {
        return localTime.toNanoOfDay() / 1000;
    }

    @Override
    protected LocalTime toSourceNotNull(Long aLong) {
        return LocalTime.ofNanoOfDay(aLong * 1000);
    }

}
