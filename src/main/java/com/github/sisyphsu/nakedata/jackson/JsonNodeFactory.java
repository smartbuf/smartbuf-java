package com.github.sisyphsu.nakedata.jackson;

import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 自定义JsonNodeFactory, 替换默认的ObjectNode实现
 *
 * @author sulin
 * @since 2019-05-03 18:43:58
 */
public class JsonNodeFactory extends com.fasterxml.jackson.databind.node.JsonNodeFactory {

    @Override
    public ObjectNode objectNode() {
        return new com.github.sisyphsu.nakedata.jackson.ObjectNode(this);
    }

}
