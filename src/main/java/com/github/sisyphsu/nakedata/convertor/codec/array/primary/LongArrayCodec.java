package com.github.sisyphsu.nakedata.convertor.codec.array.primary;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.LongArrayNode;

/**
 * Codec for long[]
 *
 * @author sulin
 * @since 2019-05-13 18:51:22
 */
public class LongArrayCodec extends Codec<long[]> {

    @Override
    public Node toNode(long[] longs) {
        return LongArrayNode.valueOf(longs);
    }

}
