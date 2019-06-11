package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
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
     * Get the real ContextType of items, only exists when dataType == Object
     *
     * @return array's ContextType
     */
    public abstract ContextType contextType();

    public boolean check(Object o) {
        // TODO 数组与o是否类型兼容
        return true;
    }

    public boolean isFix() {
        // TODO 数组是否可变
        return false;
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
