package com.github.sisyphsu.nakedata.codec.time;

import com.github.sisyphsu.nakedata.codec.VarintCodec;
import com.github.sisyphsu.nakedata.node.NodeMapper;

import java.time.OffsetDateTime;

/**
 * {@link OffsetDateTime}类型适配器
 *
 * @author sulin
 * @since 2019-05-10 10:45:30
 */
public class OffsetDateTimeCodec extends VarintCodec<OffsetDateTime> {

    public OffsetDateTimeCodec(NodeMapper mapper) {
        super(OffsetDateTime.class, mapper);
    }

    @Override
    public Long doEncode(OffsetDateTime obj) {
        return null;
    }

    @Override
    public OffsetDateTime doDecode(Long l) {
        return null;
    }

}
