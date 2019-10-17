package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.DataType;
import com.github.sisyphsu.nakedata.node.Node;

import java.util.Collections;
import java.util.List;

/**
 * Slice represent an subarray of ArrayNode.
 *
 * @author sulin
 * @since 2019-06-05 11:39:47
 */
public class ArrayNode extends Node {

    public static final ArrayNode NULL  = new ArrayNode(Collections.emptyList());
    public static final ArrayNode EMPTY = new ArrayNode(Collections.emptyList());

    private final List<SliceNode> slices;

    public ArrayNode(SliceNode node) {
        this.slices = Collections.singletonList(node);
    }

    public ArrayNode(List<SliceNode> slices) {
        this.slices = slices;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

    @Override
    public DataType dataType() {
        return DataType.ARRAY;
    }

    public List<SliceNode> getSlices() {
        return slices;
    }

}
