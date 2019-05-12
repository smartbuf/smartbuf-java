package com.github.sisyphsu.nakedata.codec.time;

import com.github.sisyphsu.nakedata.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.NodeMapper;

import java.time.Period;

/**
 * {@link Period}类型适配器
 *
 * @author sulin
 * @since 2019-05-10 10:46:24
 */
public class PeriodCodec extends Codec<Period> {

    public PeriodCodec(NodeMapper mapper) {
        super(Period.class, mapper);
    }

    @Override
    public Node encode(Period obj) {
        return null;
    }

    @Override
    public Period decode(Node node) {
        return null;
    }
    
}
