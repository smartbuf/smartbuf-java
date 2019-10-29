package com.github.sisyphsu.canoe.node;

import com.github.sisyphsu.canoe.convertor.Codec;
import com.github.sisyphsu.canoe.convertor.Converter;
import com.github.sisyphsu.canoe.node.std.ObjectNode;
import net.sf.cglib.beans.BeanMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Object's Codec, convert Node from/to Map
 *
 * @author sulin
 * @since 2019-06-05 20:29:40
 */
public final class BeanNodeCodec extends Codec {

    private static final Map<Class, BeanKey> FIELDS_MAP = new ConcurrentHashMap<>();

//    /**
//     * Convert POJO to ObjectNode, for better performance
//     */
//    @Converter(distance = 1 << 16)
//    public Node toNode(Object pojo) {
//        return null;
//    }

    /**
     * encode map to ObjectNode, pojo should be encoded as map first.
     *
     * @param map Map
     * @return ObjectNode
     */
    @Converter
    public Node toNode(Map<?, ?> map) {
        final int len = map.size();
        if (len == 0) {
            return ObjectNode.EMPTY;
        }
        boolean stable = map instanceof BeanMap;
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
            values[offset] = convert(entry.getValue(), Node.class);
            offset++;
        }
        return new ObjectNode(stable, keys, values);
    }

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
     * Get the sorted names from the specified Map, which maybe BeanMap
     *
     * @param map Map
     * @return names as array
     */
    public static BeanKey parseBeanMapKey(BeanMap map) {
        Class beanCls = map.getBean().getClass();
        BeanKey objectKey = FIELDS_MAP.get(beanCls);
        if (objectKey == null) {
            String[] fieldNames = new String[map.size()];
            int i = 0;
            for (Object key : map.keySet()) {
                String fieldName = String.valueOf(key);
                fieldNames[i++] = fieldName;
            }
            Arrays.sort(fieldNames);
            objectKey = new BeanKey(true, fieldNames);
            FIELDS_MAP.put(beanCls, objectKey);
        }
        return objectKey;
    }

    private static class BeanKey {
        private final boolean  stable;
        private final String[] fieldNames;

        public BeanKey(boolean stable, String[] fieldNames) {
            this.stable = stable;
            this.fieldNames = fieldNames;
        }
    }

}
