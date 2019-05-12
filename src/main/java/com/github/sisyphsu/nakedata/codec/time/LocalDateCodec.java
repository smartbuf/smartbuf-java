package com.github.sisyphsu.nakedata.codec.time;

import com.github.sisyphsu.nakedata.codec.VarintCodec;
import com.github.sisyphsu.nakedata.node.NodeMapper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * {@link LocalDate}类型适配器
 *
 * @author sulin
 * @since 2019-05-10 10:44:06
 */
public class LocalDateCodec extends VarintCodec<LocalDate> {

    public LocalDateCodec(NodeMapper mapper) {
        super(LocalDate.class, mapper);
    }


    @Override
    public Long doEncode(LocalDate obj) {
        return obj.toEpochDay() * 1000;
    }

    @Override
    public LocalDate doDecode(Long l) {
        return Instant.ofEpochMilli(l).atZone(ZoneId.systemDefault()).toLocalDate();
    }

}
