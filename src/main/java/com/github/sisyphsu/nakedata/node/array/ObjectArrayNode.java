package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.ArrayType;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.DataType;

import java.util.List;

/**
 * ObjectArrayNode represents an slice of similar object, which have same type.
 *
 * @author sulin
 * @since 2019-06-05 16:06:44
 */
public final class ObjectArrayNode extends ArrayNode {

    private DataType elType;

    private String[] fields;

    public ObjectArrayNode(List items) {
        super(items);
        this.elType = ((Node) items.get(0)).dataType();
    }

    @Override
    public ArrayType elementType() {
        return ArrayType.OBJECT;
    }

    public String[] getFields() {
        return fields;
    }

}
