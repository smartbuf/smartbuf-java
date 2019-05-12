package com.github.sisyphsu.nakedata.codec.time;

import com.github.sisyphsu.nakedata.codec.VarintCodec;
import com.github.sisyphsu.nakedata.node.NodeMapper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * {@link LocalDateTime}类型适配器
 *
 * @author sulin
 * @since 2019-05-10 10:43:53
 */
public class LocalDateTimeCodec extends VarintCodec<LocalDateTime> {

    public LocalDateTimeCodec(NodeMapper mapper) {
        super(LocalDateTime.class, mapper);
    }

    @Override
    public Long doEncode(LocalDateTime obj) {
        return obj.toInstant(ZoneOffset.UTC).toEpochMilli();
    }

    @Override
    public LocalDateTime doDecode(Long l) {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneOffset.UTC);
    }

}
