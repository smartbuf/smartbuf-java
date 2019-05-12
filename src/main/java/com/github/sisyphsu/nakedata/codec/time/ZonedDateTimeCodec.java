package com.github.sisyphsu.nakedata.codec.time;

import com.github.sisyphsu.nakedata.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.NodeMapper;

import java.time.ZonedDateTime;

/**
 * {@link ZonedDateTime}类型适配器
 *
 * @author sulin
 * @since 2019-05-10 10:44:59
 */
public class ZonedDateTimeCodec extends Codec<ZonedDateTime> {

    public ZonedDateTimeCodec(NodeMapper mapper) {
        super(ZonedDateTime.class, mapper);
    }

    @Override
    public Node encode(ZonedDateTime obj) {
        return null;
    }

    @Override
    public ZonedDateTime decode(Node node) {
        return null;
    }

}
