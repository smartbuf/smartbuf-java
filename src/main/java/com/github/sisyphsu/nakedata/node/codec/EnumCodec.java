package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.std.SymbolNode;

/**
 * @author sulin
 * @since 2019-06-10 21:15:13
 */
public class EnumCodec extends Codec {

    public Node toNode(Enum e) {
        String name = e.name();

        return SymbolNode.valueOf(name);
    }

}
