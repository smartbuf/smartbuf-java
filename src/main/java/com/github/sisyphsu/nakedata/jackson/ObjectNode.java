package com.github.sisyphsu.nakedata.jackson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.sisyphsu.nakedata.context.model.ContextType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * 拓展默认的ObjectNode
 *
 * @author sulin
 * @since 2019-05-03 18:42:10
 */
@Getter
@Setter
public class ObjectNode extends com.fasterxml.jackson.databind.node.ObjectNode {

    /**
     * 此对象的上下文类型
     */
    private ContextType type;

    public ObjectNode(JsonNodeFactory nc) {
        super(nc);
    }

    public ObjectNode(JsonNodeFactory nc, Map<String, JsonNode> kids) {
        super(nc, kids);
    }

}
