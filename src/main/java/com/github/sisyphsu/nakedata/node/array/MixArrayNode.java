package com.github.sisyphsu.nakedata.node.array;

import java.util.List;

/**
 * MixArrayNode represent an intact Object array, which was split into multi slice by real type.
 *
 * @author sulin
 * @since 2019-05-08 21:02:18
 */
public class MixArrayNode extends ArrayNode {

    public MixArrayNode(List<ArrayNode> items) {
        super(items);
    }

    @Override
    public int size() {
        int result = 0;
        for (Object item : items) {
            ArrayNode node = (ArrayNode) item;
            result += node.size();
        }
        return result;
    }

}
