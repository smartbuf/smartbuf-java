package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.std.ObjectNode;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Object's Codec
 *
 * @author sulin
 * @since 2019-06-05 20:29:40
 */
public class ObjectCodec extends Codec {

    /**
     * encode map to ObjectNode, pojo should be encoded as map first.
     *
     * @param map Map
     * @return ObjectNode
     */
    public Node toNode(Map map) {
        if (map == null) {
            return ObjectNode.NULL;
        }
        if (map.isEmpty()) {
            return ObjectNode.EMPTY;
        }
        TreeMap<String, Node> fields = new TreeMap<>();
        for (Object item : map.entrySet()) {
            Map.Entry entry = (Map.Entry) item;
            String key;
            if (entry.getKey() instanceof String) {
                key = (String) entry.getKey();
            } else {
                key = convert(entry.getKey(), String.class);
            }
            Node value = convert(entry.getValue(), Node.class);

            fields.put(key, value);
        }
        return ObjectNode.valueOf(fields);
    }

    /**
     * decode ObjectNode to map, expose fields directly.
     *
     * @param node ObjectNode
     * @return Map
     */
    public Map toMap(ObjectNode node) {
        if (node == ObjectNode.NULL) {
            return null;
        }
        if (node == ObjectNode.EMPTY) {
            return new HashMap();
        }
        return node.getFields();
    }

}
