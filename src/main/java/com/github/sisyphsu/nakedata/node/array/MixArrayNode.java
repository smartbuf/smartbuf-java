package com.github.sisyphsu.nakedata.node.array;

import java.util.List;

/**
 * MixArrayNode represent an intact Object array, which was split into multi slice by real type.
 *
 * @author sulin
 * @since 2019-05-08 21:02:18
 */
public final class MixArrayNode extends ArrayNode {

    public MixArrayNode(List<ArrayNode> items) {
        super(items, null);
    }

}
