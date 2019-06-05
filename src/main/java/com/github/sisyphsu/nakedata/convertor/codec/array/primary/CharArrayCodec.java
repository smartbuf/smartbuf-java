package com.github.sisyphsu.nakedata.convertor.codec.array.primary;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.std.StringNode;

/**
 * Codec for char[]
 *
 * @author sulin
 * @since 2019-05-13 18:52:12
 */
public class CharArrayCodec extends Codec<char[]> {

    @Override
    public Node toNode(char[] chars) {
        return StringNode.valueOf(String.valueOf(chars));
    }

}
