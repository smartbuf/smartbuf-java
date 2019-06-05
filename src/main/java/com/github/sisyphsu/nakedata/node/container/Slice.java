package com.github.sisyphsu.nakedata.node.container;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * Slice represent an subarray of ArrayNode.
 *
 * @author sulin
 * @since 2019-06-05 11:39:47
 */
public abstract class Slice {

    /**
     * Get slice's size
     *
     * @return real size
     */
    public abstract int size();

    /**
     * Get slice's dataType
     *
     * @return item's DataType
     */
    public abstract DataType dataType();

    /**
     * Get the real ContextType of items, only exists when dataType == Object
     *
     * @return slice's ContextType
     */
    public abstract ContextType contextType();

}
