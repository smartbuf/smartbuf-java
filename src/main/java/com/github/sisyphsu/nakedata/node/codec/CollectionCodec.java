package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.node.array.MixArrayNode;
import com.github.sisyphsu.nakedata.node.array.ObjectArrayNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        List<ArrayNode> arrays = new ArrayList<>(4);
        ArrayNode last = null;
        for (Object item : coll) {
            if (last != null && !last.check(item)) {
                last = null;
            }
            if (last == null) {
                last = (ArrayNode) convert(item, ArrayNode.class);
                arrays.add(last);
            }
            // TODO arrayNode.append
        }
        if (arrays.size() > 1) {
            return MixArrayNode.valueOf(arrays);
        }
        return last;
    }

}
