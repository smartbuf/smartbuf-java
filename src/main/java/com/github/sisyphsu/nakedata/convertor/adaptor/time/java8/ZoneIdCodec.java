package com.github.sisyphsu.nakedata.convertor.adaptor.time.java8;

import com.github.sisyphsu.nakedata.convertor.adaptor.Codec;

import java.time.ZoneId;

/**
 * {@link ZoneId}与{@link String}的数据转换适配器
 *
 * @author sulin
 * @since 2019-05-10 10:46:11
 */
public class ZoneIdCodec extends Codec<ZoneId> {

    protected String toTargetNotNull(ZoneId zoneId) {
        return zoneId.getId();
    }

    protected ZoneId toSourceNotNull(String s) {
        return ZoneId.of(s);
    }

}
