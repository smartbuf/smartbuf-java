package com.github.sisyphsu.nakedata.convertor.codec.map;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.jar.Attributes;

/**
 * Map's codec
 *
 * @author sulin
 * @since 2019-05-13 18:30:41
 */
public class MapCodec extends Codec {

    /**
     * Convert Map to Map with the specified generic type
     *
     * @param map  Source Map
     * @param type Generic Type
     * @return Map
     */
    public Map toMap(Map<?, ?> map, XType<?> type) {
        if (map == null)
            return null;
        Class<?> rawType = type.getRawType();
        XType<?>[] paramTypes = type.getParameterizedTypes();
        XType<?> keyType = paramTypes[0];
        XType<?> valType = paramTypes[1];
        // check compatible, avoid unnessery copy

        // prepare map
        Map result;
        if (rawType.isAssignableFrom(EnumMap.class)) {
            result = new EnumMap(keyType.getRawType());
        } else if (rawType.isAssignableFrom(HashMap.class)) {
            result = new HashMap();
        } else if (rawType.isAssignableFrom(Hashtable.class)) {
            result = new Hashtable();
        } else if (rawType.isAssignableFrom(IdentityHashMap.class)) {
            result = new IdentityHashMap();
        } else if (rawType.isAssignableFrom(LinkedHashMap.class)) {
            result = new LinkedHashMap();
        } else if (rawType.isAssignableFrom(Properties.class)) {
            result = new Properties();
        } else if (rawType.isAssignableFrom(TreeMap.class)) {
            result = new TreeMap();
        } else if (rawType.isAssignableFrom(WeakHashMap.class)) {
            result = new WeakHashMap(map.size());
        } else if (rawType.isAssignableFrom(ConcurrentHashMap.class)) {
            result = new ConcurrentHashMap(map.size());
        } else if (rawType.isAssignableFrom(ConcurrentSkipListMap.class)) {
            result = new ConcurrentSkipListMap();
        } else if (rawType.isAssignableFrom(Attributes.class)) {
            result = new Attributes(map.size());
        } else {
            throw new RuntimeException("Unsupport Map: " + type.getRawType());
        }
        // copy entries
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = convert(entry.getKey(), keyType);
            Object val = convert(entry.getValue(), valType);
            result.put(key, val);
        }
        return result;
    }

}
