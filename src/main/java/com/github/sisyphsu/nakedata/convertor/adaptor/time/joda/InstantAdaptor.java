package com.github.sisyphsu.nakedata.convertor.adaptor.time.joda;

import com.github.sisyphsu.nakedata.convertor.adaptor.Adaptor;

import java.time.Instant;

/**
 * {@link Instant}与{@link Long}的数据转换适配器
 *
 * @author sulin
 * @since 2019-05-10 10:46:44
 */
public class InstantAdaptor extends Adaptor<Instant, Long> {

    @Override
    protected Long toTargetNotNull(Instant instant) {
        return instant.toEpochMilli();
    }

    @Override
    protected Instant toSourceNotNull(Long aLong) {
        return Instant.ofEpochMilli(aLong);
    }

}
