package com.github.sisyphsu.nakedata.node.array.primary;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.array.ArrayNode;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * float[] array
 *
 * @author sulin
 * @since 2019-06-04 16:34:25
 */
public class FArrayNode extends ArrayNode {

    public static final FArrayNode NULL = new FArrayNode(null);
    public static final FArrayNode EMPTY = new FArrayNode(new float[0]);

    private float[] items;

    private FArrayNode(float[] items) {
        this.items = items;
    }

    public static FArrayNode valueOf(float[] data) {
        if (data == null) {
            return NULL;
        }
        if (data.length == 0) {
            return EMPTY;
        }
        return new FArrayNode(data);
    }

    @Override
    public int size() {
        return items.length;
    }

    @Override
    public DataType dataType() {
        return DataType.FLOAT;
    }

    @Override
    public ContextType contextType() {
        return null;
    }
}
