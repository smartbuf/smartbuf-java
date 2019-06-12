package com.github.sisyphsu.nakedata.node.array;

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
public class MixArrayNode extends ArrayNode {

    public final static MixArrayNode NULL = new MixArrayNode(null);

    private final List<ArrayNode> items;

    private MixArrayNode(List<ArrayNode> slices) {
        this.items = slices;
    }

    public static MixArrayNode valueOf(List<ArrayNode> items) {
        if (items == null)
            return NULL;
        return new MixArrayNode(items);
    }

    public static MixArrayNode valueOf(ArrayNode slice) {
        if (slice == null) {
            return NULL;
        }
        List<ArrayNode> slices = new ArrayList<>();
        slices.add(slice);
        return valueOf(slices);
    }

    @Override
    public int size() {
        int result = 0;
        for (ArrayNode item : items) {
            result += item.size();
        }
        return result;
    }

    @Override
    public boolean tryAppend(Object o) {
        return false;
    }

}
