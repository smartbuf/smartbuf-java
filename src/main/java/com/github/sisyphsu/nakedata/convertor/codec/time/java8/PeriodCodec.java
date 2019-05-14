package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.time.Period;

/**
 * {@link Period}与{@link String}类型转换适配器
 *
 * @author sulin
 * @since 2019-05-10 10:46:24
 */
public class PeriodCodec extends Codec<Period> {

    protected String toTargetNotNull(Period period) {
        return null;
    }

    protected Period toSourceNotNull(String s) {
        return null;
    }

}
