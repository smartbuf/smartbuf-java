package com.github.sisyphsu.canoe.node;

import com.github.sisyphsu.canoe.convertor.Codec;
import com.github.sisyphsu.canoe.convertor.CodecContext;
import com.github.sisyphsu.canoe.convertor.Converter;
import com.github.sisyphsu.canoe.exception.CircleReferenceException;
import com.github.sisyphsu.canoe.node.std.*;
import com.github.sisyphsu.canoe.reflect.BeanHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Object's Codec, convert Node from/to Map
 *
 * @author sulin
 * @since 2019-06-05 20:29:40
 */
@SuppressWarnings("unchecked")
public final class BeanNodeCodec extends Codec {

    /**
     * decode ObjectNode to map, expose fields directly.
     *
     * @param node ObjectNode
     * @return Map
     */
    @Converter
    public Map<String, Node> toMap(ObjectNode node) {
        Map<String, Node> result = new HashMap<>();
        if (node != ObjectNode.EMPTY) {
            String[] keys = node.keys();
            Object[] values = node.values();
            for (int i = 0, len = keys.length; i < len; i++) {
                result.put(keys[i], (Node) values[i]);
            }
        }
        return result;
    }

    /**
     * encode map to ObjectNode, pojo should be encoded as map first.
     *
     * @param map Map
     * @return ObjectNode
     */
    @Converter
    public Node toNode(Map<?, ?> map) {
        int len = map.size();
        if (len == 0) {
            return ObjectNode.EMPTY;
        }
        String[] keys = new String[len];
        Object[] values = new Object[len];
        int offset = 0;
        for (Object item : map.entrySet()) {
            Map.Entry entry = (Map.Entry) item;
            String key;
            if (entry.getKey() instanceof String) {
                key = (String) entry.getKey();
            } else {
                key = convert(entry.getKey(), String.class);
            }
            keys[offset] = key;
            values[offset] = entry.getValue();
            offset++;
        }
        return buildObjectNode(false, keys, values);
    }

    /**
     * Convert POJO to ObjectNode, for better performance
     */
    @Converter(distance = 1 << 16)
    public Node toNode(Object pojo) {
        // check loop references
        CodecContext state = CodecContext.get();
        if (state.depth() > 64 && !state.record(pojo)) {
            throw new CircleReferenceException();
        }
        BeanHelper helper = BeanHelper.valueOf(pojo.getClass());
        String[] names = helper.getNames();
        Object[] values = helper.getValues(pojo);
        return buildObjectNode(true, names, values);
    }

    // build ObjectNode
    private ObjectNode buildObjectNode(boolean stable, String[] keys, Object[] values) {
        for (int i = 0, len = values.length; i < len; i++) {
            Object o = values[i];
            if (o == null || o instanceof Boolean || o instanceof Float || o instanceof Double || o instanceof Long || o instanceof String) {
                continue;
            }
            if (o instanceof CharSequence) {
                values[i] = o.toString();
                continue;
            }
            if (o instanceof Byte || o instanceof Short || o instanceof Integer) {
                values[i] = ((Number) o).longValue();
                continue;
            }
            Node node = convert(o, Node.class);
            if (node instanceof BooleanNode) {
                values[i] = node.booleanValue();
            } else if (node instanceof FloatNode) {
                values[i] = node.floatValue();
            } else if (node instanceof DoubleNode) {
                values[i] = node.doubleValue();
            } else if (node instanceof VarintNode) {
                values[i] = node.longValue();
            } else if (node instanceof StringNode) {
                values[i] = node.stringValue();
            } else {
                values[i] = node;
            }
        }
        return new ObjectNode(stable, keys, values);
    }

}
