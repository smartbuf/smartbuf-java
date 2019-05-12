package com.github.sisyphsu.nakedata.codec.time;

import com.github.sisyphsu.nakedata.codec.VarintCodec;
import com.github.sisyphsu.nakedata.node.NodeMapper;

import java.time.Instant;

/**
 * {@link Instant}数据类型适配
 *
 * @author sulin
 * @since 2019-05-10 10:46:44
 */
public class InstantCodec extends VarintCodec<Instant> {

    public InstantCodec(NodeMapper mapper) {
        super(Instant.class, mapper);
    }

    @Override
    public Long doEncode(Instant obj) {
        return obj.toEpochMilli();
    }

    @Override
    public Instant doDecode(Long l) {
        return Instant.ofEpochMilli(l);
    }

}
