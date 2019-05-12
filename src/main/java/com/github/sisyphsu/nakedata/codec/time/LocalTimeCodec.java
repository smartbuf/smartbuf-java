package com.github.sisyphsu.nakedata.codec.time;

import com.github.sisyphsu.nakedata.codec.VarintCodec;
import com.github.sisyphsu.nakedata.node.NodeMapper;

import java.time.LocalTime;

/**
 * {@link LocalTime}类型适配器
 *
 * @author sulin
 * @since 2019-05-10 10:44:44
 */
public class LocalTimeCodec extends VarintCodec<LocalTime> {

    public LocalTimeCodec(NodeMapper mapper) {
        super(LocalTime.class, mapper);
    }

    @Override
    public Long doEncode(LocalTime obj) {
        return obj.toNanoOfDay() / 1000;
    }

    @Override
    public LocalTime doDecode(Long l) {
        return LocalTime.ofNanoOfDay(l * 1000);
    }

}
