package com.github.sisyphsu.nakedata;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.sisyphsu.nakedata.utils.JSONUtils;

/**
 * 1. scan object's type
 * 2. adjust namePool
 * 3. adjust typePool
 * 4. serialize data: name, type, content
 *
 * @author sulin
 * @since 2019-04-24 21:43:29
 */
public class NDataSerializer {

    public void serialize(Object obj) {
        JsonNode node = JSONUtils.toJsonNode(obj);
        if (node == null) {
            return;
        }
        switch (node.getNodeType()) {
            case NULL:
            case BOOLEAN:
            case NUMBER:
            case BINARY:
            case STRING:
            case ARRAY:
            case OBJECT:
            default:
        }
    }

}
