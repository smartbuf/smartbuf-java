package com.github.sisyphsu.nakedata.node.container;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.type.DataType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sulin
 * @since 2019-05-08 21:02:18
 */
@Getter
@Setter
public class ArrayNode extends Node {

    public final static ArrayNode NULL = new ArrayNode(null);

    private final List<SliceNode> items;

    private ArrayNode(List<SliceNode> slices) {
        this.items = slices;
    }

    public static ArrayNode valueOf(List<SliceNode> items) {
        if (items == null)
            return NULL;
        return new ArrayNode(items);
    }

    public static ArrayNode valueOf(SliceNode slice) {
        if (slice == null) {
            return NULL;
        }
        List<SliceNode> slices = new ArrayList<>();
        slices.add(slice);
        return valueOf(slices);
    }

    @Override
    public DataType dataType() {
        return DataType.ARRAY;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

}
