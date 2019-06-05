package com.github.sisyphsu.nakedata.convertor.codec.array.primary;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.IntArrayNode;

/**
 * Codec for int[]
 *
 * @author sulin
 * @since 2019-05-13 18:51:01
 */
public class IntArrayCodec extends Codec<int[]> {

    @Override
    public Node toNode(int[] ints) {
        return IntArrayNode.valueOf(ints);
    }

}
