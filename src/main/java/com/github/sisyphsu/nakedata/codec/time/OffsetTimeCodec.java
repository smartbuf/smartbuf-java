package com.github.sisyphsu.nakedata.codec.time;

import com.github.sisyphsu.nakedata.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.NodeMapper;

import java.time.OffsetTime;

/**
 * {@link OffsetTime}类型适配器
 *
 * @author sulin
 * @since 2019-05-10 10:45:58
 */
public class OffsetTimeCodec extends Codec<OffsetTime> {

    public OffsetTimeCodec(NodeMapper mapper) {
        super(OffsetTime.class, mapper);
    }

    @Override
    public Node encode(OffsetTime obj) {
        return null;
    }

    @Override
    public OffsetTime decode(Node node) {
        return null;
    }

}
