package com.github.sisyphsu.nakedata.node.codec;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.std.ObjectNode;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author sulin
 * @since 2019-06-05 20:29:40
 */
public class MapCodec extends Codec<Map> {

    public Node toNode(Map map) {
        if (map == null) {
            return ObjectNode.NULL;
        }
        TreeMap<String, Node> fields = new TreeMap<>();
        for (Object item : map.entrySet()) {
            Map.Entry entry = (Map.Entry) item;
            String key = String.valueOf(entry.getKey()); // TODO key -> string
            entry.getValue(); // TODO val -> node

            fields.put(key, null);
        }
        return new ObjectNode(fields);
    }

}
