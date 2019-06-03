package com.github.sisyphsu.nakedata.convertor.codec.array;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.std.BinaryNode;

/**
 * @author sulin
 * @since 2019-05-13 18:51:46
 */
public class ByteArrayCodec extends Codec<byte[]> {

    @Override
    public Node toNode(byte[] bytes) {
        return BinaryNode.valueOf(bytes);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
