package com.github.sisyphsu.nakedata.jackson.node;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.sisyphsu.nakedata.DataType;

/**
 * @author sulin
 * @since 2019-05-06 16:57:31
 */
public class NArrayNode extends ArrayNode implements DataType {

    public NArrayNode(JsonNodeFactory nf) {
        super(nf);
    }

    public NArrayNode(JsonNodeFactory nf, int capacity) {
        super(nf, capacity);
    }

    @Override
    public byte getTypeCode() {
        return ARRAY;
    }

}
