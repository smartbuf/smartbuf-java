package com.github.sisyphsu.nakedata.jackson.node;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.sisyphsu.nakedata.DataType;
import com.github.sisyphsu.nakedata.context.model.ContextType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.TreeMap;

/**
 * 拓展默认的ObjectNode
 *
 * @author sulin
 * @since 2019-05-03 18:42:10
 */
@Getter
@Setter
public class NObjectNode extends ObjectNode implements DataType {

    private ContextType type;

    public NObjectNode(JsonNodeFactory factory) {
        super(factory, new TreeMap<>());
    }

    @Override
    public byte getTypeCode() {
        return OBJECT;
    }

    public Map<String, JsonNode> getFields() {
        return super._children;
    }

}
