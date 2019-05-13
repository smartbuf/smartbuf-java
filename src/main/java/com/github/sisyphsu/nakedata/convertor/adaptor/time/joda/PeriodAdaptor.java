package com.github.sisyphsu.nakedata.convertor.adaptor.time.joda;

import com.github.sisyphsu.nakedata.convertor.adaptor.Adaptor;

import java.time.Period;

/**
 * {@link Period}与{@link String}类型转换适配器
 *
 * @author sulin
 * @since 2019-05-10 10:46:24
 */
public class PeriodAdaptor extends Adaptor<Period, String> {

    @Override
    protected String toTargetNotNull(Period period) {
        return null;
    }

    @Override
    protected Period toSourceNotNull(String s) {
        return null;
    }

}
