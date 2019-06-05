package com.github.sisyphsu.nakedata.node.container.slice;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.container.Slice;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * null slice, only record count.
 *
 * @author sulin
 * @since 2019-06-05 16:07:43
 */
public class NullSlice extends Slice {

    private int count;

    public NullSlice(int count) {
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
