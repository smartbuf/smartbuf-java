package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.time.LocalTime;

/**
 * {@link LocalTime}与{@link Long}的类型转换适配器
 *
 * @author sulin
 * @since 2019-05-10 10:44:44
 */
public class LocalTimeCodec extends Codec<LocalTime> {

    protected Long toTargetNotNull(LocalTime localTime) {
        return localTime.toNanoOfDay() / 1000;
    }

    protected LocalTime toSourceNotNull(Long aLong) {
        return LocalTime.ofNanoOfDay(aLong * 1000);
    }

}
