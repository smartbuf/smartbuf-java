package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * Slice represent an subarray of ArrayNode.
 *
 * @author sulin
 * @since 2019-06-05 11:39:47
 */
public abstract class ArrayNode extends Node {

    /**
     * Get array's size
     *
     * @return real size
     */
    public abstract int size();

    /**
     * Try to append new object into this array.
     *
     * @param o new object
     * @return Whether success or not
     */
    public abstract boolean tryAppend(Object o);

    /**
     * Get Array's element dataType
     *
     * @return DataType of element
     */
    public DataType elementDataType() {
        return null;
    }

    /**
     * Get the real ContextType of items, only exists when dataType == Object
     *
     * @return array's ContextType
     */
    public Object elementContextType() {
        return null;
    }

    @Override
    public DataType dataType() {
        return DataType.ARRAY;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
