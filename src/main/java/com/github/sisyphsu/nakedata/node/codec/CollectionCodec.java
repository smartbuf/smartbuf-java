package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.*;

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
     * @param list Collection
     * @return ArrayNode
     */
    public Node toNode(List list) {
        if (list == null) {
            return ArrayNode.NULL;
        }
        if (list.isEmpty()) {
            return ArrayNode.EMPTY;
        }
        ArrayNode singleSliceResult = null;
        List<ArrayNode> multiSliceResult = null;
        // iterate list and generate slices.
        Class sliceType = null;
        for (int i = 0, sliceOff = 0, size = list.size(); i < size; i++) {
            Object curr = list.get(i);
            Class type = curr == null ? null : curr.getClass();
            // do nothing for first item
            if (i == 0) {
                sliceType = type;
                continue;
            }
            // continue if continuously and not end
            if (sliceType == type && i < size - 1) {
                continue;
            }
            // generate ArrayNode as slice
            ArrayNode slice;
            List subList = list.subList(sliceOff, i);
            if (sliceType == null) {
                slice = new NullArrayNode(subList);
            } else if (sliceType == Boolean.class) {
                slice = new BooleanArrayNode(subList);
            } else if (sliceType == Byte.class) {
                slice = new ByteArrayNode(subList);
            } else if (sliceType == Short.class) {
                slice = new ShortArrayNode(subList);
            } else if (sliceType == Integer.class) {
                slice = new IntegerArrayNode(subList);
            } else if (sliceType == Long.class) {
                slice = new LongArrayNode(subList);
            } else if (sliceType == Float.class) {
                slice = new FloatArrayNode(subList);
            } else if (sliceType == Double.class) {
                slice = new DoubleArrayNode(subList);
            } else if (sliceType == String.class) {
                slice = new StringArrayNode(subList);
            } else {
                List<Node> nodes = new ArrayList<>(subList.size());
                for (Object o : subList) {
                    nodes.add((Node) convert(o, Node.class));
                }
                slice = new ObjectArrayNode(nodes);
            }
            // update arrays and last
            if (i == size - 1 && multiSliceResult == null) {
                singleSliceResult = slice;
            } else {
                if (multiSliceResult == null) {
                    multiSliceResult = new ArrayList<>();
                }
                multiSliceResult.add(slice);
            }
            // update next slice
            sliceOff = i;
            sliceType = type;
        }
        if (singleSliceResult != null) {
            return singleSliceResult;
        }
        return new MixArrayNode(multiSliceResult);
    }

    public Collection toCollection(ArrayNode node) {
        // 应该直接转换为Array, 然后再按需转换为Collection的子类
        return null;
    }

}
