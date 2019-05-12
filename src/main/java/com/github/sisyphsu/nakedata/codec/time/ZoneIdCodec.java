package com.github.sisyphsu.nakedata.codec.time;

import com.github.sisyphsu.nakedata.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.NodeMapper;

import java.time.ZoneId;

/**
 * {@link ZoneId}类型适配器
 *
 * @author sulin
 * @since 2019-05-10 10:46:11
 */
public class ZoneIdCodec extends Codec<ZoneId> {

    public ZoneIdCodec(NodeMapper mapper) {
        super(ZoneId.class, mapper);
    }

    @Override
    public Node encode(ZoneId obj) {
        return null;
    }

    @Override
    public ZoneId decode(Node node) {
        return null;
    }

}
