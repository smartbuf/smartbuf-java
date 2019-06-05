package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * float[] array
 *
 * @author sulin
 * @since 2019-06-04 16:34:25
 */
public class FloatArrayNode extends ArrayNode {

    public static final FloatArrayNode NULL = new FloatArrayNode(null);
    public static final FloatArrayNode EMPTY = new FloatArrayNode(new float[0]);

    private float[] items;

    private FloatArrayNode(float[] items) {
        this.items = items;
    }

    public static FloatArrayNode valueOf(float[] data) {
        if (data == null) {
            return NULL;
        }
        if (data.length == 0) {
            return EMPTY;
        }
        return new FloatArrayNode(data);
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
