package com.github.sisyphsu.nakedata.jackson.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.sisyphsu.nakedata.DataType;

import java.util.List;

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

    public NArrayNode(JsonNodeFactory nf, List<JsonNode> children) {
        super(nf, children);
    }

    @Override
    public byte getTypeCode() {
        return ARRAY;
    }

}
