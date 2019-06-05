package com.github.sisyphsu.nakedata.node.container;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.container.ArrayNode;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * null array, only record count.
 *
 * @author sulin
 * @since 2019-06-05 16:07:43
 */
public class NullArrayNode extends ArrayNode {

    private int count;

    public NullArrayNode(int count) {
        this.count = count;
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public DataType dataType() {
        return DataType.NULL;
    }

    @Override
    public ContextType contextType() {
        return null;
    }

}
