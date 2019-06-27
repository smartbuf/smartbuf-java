package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.node.array.MixArrayNode;

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

    /**
     * encode collection to ArrayNode
     *
     * @param coll Collection
     * @return ArrayNode
     */
    public Node toNode(Collection coll) {
        if (coll == null) {
            return null;
        }
        List<ArrayNode> arrays = new ArrayList<>();
        ArrayNode last = null;
        for (Object item : coll) {
            if (last != null && !last.tryAppend(item)) {
                last = null;
            }
            if (last == null) {
                last = (ArrayNode) convert(item, ArrayNode.class);
                last.tryAppend(item);
                arrays.add(last);
            }
        }
        if (arrays.size() > 1) {
            return MixArrayNode.valueOf(arrays);
        }
        return last;
    }

    public Collection toCollection(ArrayNode node) {
        // 应该直接转换为Array, 然后再按需转换为Collection的子类
        return null;
    }

}
