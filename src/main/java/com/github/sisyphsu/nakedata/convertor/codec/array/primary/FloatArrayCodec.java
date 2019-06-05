package com.github.sisyphsu.nakedata.convertor.codec.array.primary;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.FloatArrayNode;

/**
 * Codec for float[]
 *
 * @author sulin
 * @since 2019-05-13 18:52:31
 */
public class FloatArrayCodec extends Codec<float[]> {

    @Override
    public Node toNode(float[] floats) {
        return FloatArrayNode.valueOf(floats);
    }

}
