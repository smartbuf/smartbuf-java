package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.node.std.ObjectNode;
import net.sf.cglib.beans.BeanMap;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Object's Codec
 *
 * @author sulin
 * @since 2019-06-05 20:29:40
 */
public final class BeanNodeCodec extends Codec {

    private static final Map<Class, String[]> FIELDS_MAP = new ConcurrentHashMap<>();

    /**
     * encode map to ObjectNode, pojo should be encoded as map first.
     *
     * @param map Map
     * @return ObjectNode
     */
    @Converter
    public Node toNode(Map<?, ?> map) {
        if (map == null) {
            return ObjectNode.NULL;
        }
        if (map.isEmpty()) {
            return ObjectNode.EMPTY;
        }
        final String[] fieldNames = getFieldNames(map);
        final HashMap<String, Node> fields = new HashMap<>();
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
        return ObjectNode.valueOf(fieldNames, fields);
    }

    /**
     * decode ObjectNode to map, expose fields directly.
     *
     * @param node ObjectNode
     * @return Map
     */
    @Converter
    public Map toMap(ObjectNode node) {
        if (node == ObjectNode.NULL) {
            return null;
        }
        if (node == ObjectNode.EMPTY) {
            return new HashMap();
        }
        return node.getData();
    }

    /**
     * Get the sorted names from the specified Map, which maybe BeanMap
     *
     * @param map Map
     * @return names as array
     */
    public static String[] getFieldNames(Map map) {
        Class beanCls = null;
        if (map instanceof BeanMap) {
            beanCls = ((BeanMap) map).getBean().getClass();
            String[] fieldNames = FIELDS_MAP.get(beanCls);
            if (fieldNames != null) {
                return fieldNames;
            }
        }
        String[] fieldNames = new String[map.size()];
        int i = 0;
        for (Object key : map.keySet()) {
            fieldNames[i++] = String.valueOf(key);
        }
        Arrays.sort(fieldNames);
        if (beanCls != null) {
            FIELDS_MAP.put(beanCls, fieldNames);
        }
        return fieldNames;
    }

}
