package com.github.sisyphsu.nakedata.convertor.adaptor.time.java8;

import com.github.sisyphsu.nakedata.convertor.adaptor.Codec;

import java.time.Instant;

/**
 * {@link Instant}与{@link Long}的数据转换适配器
 *
 * @author sulin
 * @since 2019-05-10 10:46:44
 */
public class InstantCodec extends Codec<Instant> {

    protected Long toTargetNotNull(Instant instant) {
        return instant.toEpochMilli();
    }

    protected Instant toSourceNotNull(Long aLong) {
        return Instant.ofEpochMilli(aLong);
    }

}
