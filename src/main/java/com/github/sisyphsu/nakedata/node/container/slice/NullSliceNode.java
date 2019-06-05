package com.github.sisyphsu.nakedata.node.container.slice;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.container.SliceNode;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * null slice, only record count.
 *
 * @author sulin
 * @since 2019-06-05 16:07:43
 */
public class NullSliceNode extends SliceNode {

    private int count;

    public NullSliceNode(int count) {
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
