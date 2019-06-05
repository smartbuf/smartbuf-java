package com.github.sisyphsu.nakedata.node.container;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * Slice represent an subarray of ArrayNode.
 *
 * @author sulin
 * @since 2019-06-05 11:39:47
 */
public abstract class SliceNode extends Node {

    /**
     * Get slice's size
     *
     * @return real size
     */
    public abstract int size();

    /**
     * Get the real ContextType of items, only exists when dataType == Object
     *
     * @return slice's ContextType
     */
    public abstract ContextType contextType();

    @Override
    public DataType dataType() {
        return DataType.ARRAY;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
