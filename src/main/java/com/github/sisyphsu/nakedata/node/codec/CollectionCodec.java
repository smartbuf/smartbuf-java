package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.node.array.ObjectArrayNode;

import java.util.Collection;

/**
 * Collection's codec
 *
 * @author sulin
 * @since 2019-06-05 20:14:25
 */
public class CollectionCodec extends Codec {

    public Node toNode(Collection coll) {
        if (coll == null) {
            return ObjectArrayNode.NULL;
        }
        if (coll.isEmpty()) {
            return ObjectArrayNode.EMPTY;
        }
        // 1. iterate coll, group by datatype
        // 2. if multi group, return MixArray, else return NormalArray
        // TODO
        ArrayNode arrayNode = null;

        for (Object o : coll) {
            if (arrayNode == null) {
                // build
            }
        }
        return null;
    }

}
