package com.github.sisyphsu.nakedata.convertor.codec.array.primary;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.DoubleArrayNode;

/**
 * Codec for double[]
 *
 * @author sulin
 * @since 2019-05-13 18:52:43
 */
public class DoubleArrayCodec extends Codec<double[]> {

    @Override
    public Node toNode(double[] doubles) {
        return DoubleArrayNode.valueOf(doubles);
    }

}
