package com.github.sisyphsu.nakedata.convertor.codec.map;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.jar.Attributes;

/**
 * Map's codec, support Map and Map.Entry
 *
 * @author sulin
 * @since 2019-05-13 18:30:41
 */
@SuppressWarnings("unchecked")
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
        // TODO check compatible, avoid unnessery copy

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

    /**
     * Convert Map.Entry to Map, dont handle generic type
     *
     * @param entry Entry
     * @return Map
     */
    public Map toMap(Map.Entry entry) {
        if (entry == null)
            return null;
        Map map = new HashMap();
        map.put(entry.getKey(), entry.getValue());
        return map;
    }

    /**
     * Convert Map.Entry to Map.Entry with the specified generic type
     *
     * @param entry Map.Entry
     * @param type  Type
     * @return Map.Entry
     */
    public Map.Entry toMapEntry(Map.Entry<?, ?> entry, XType<?> type) {
        if (entry == null)
            return null;

        Class<?> clz = type.getRawType();
        XType<?>[] paramTypes = type.getParameterizedTypes();
        Object key = convert(entry.getKey(), paramTypes[0]);
        Object val = convert(entry.getValue(), paramTypes[1]);
        if (clz.isAssignableFrom(AbstractMap.SimpleEntry.class)) {
            return new AbstractMap.SimpleEntry(key, val);
        } else if (clz.isAssignableFrom(AbstractMap.SimpleImmutableEntry.class)) {
            return new AbstractMap.SimpleImmutableEntry(key, val);
        }
        throw new IllegalArgumentException("Unsupported Type: " + type.getRawType());
    }

    /**
     * Convert Map to Map.Entry, dont handle generic type
     *
     * @param map Map
     * @return Map.Entry
     */
    public Map.Entry toMapEntry(Map<?, ?> map) {
        if (map == null || map.isEmpty())
            return null;
        if (map.size() > 1) {
            throw new IllegalArgumentException("Can't convert Map[size > 1] to Map.Entry");
        }
        return map.entrySet().iterator().next();
    }

}
