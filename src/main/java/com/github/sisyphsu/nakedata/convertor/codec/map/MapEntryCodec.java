package com.github.sisyphsu.nakedata.convertor.codec.map;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Map.Entry's codec
 *
 * @author sulin
 * @since 2019-05-13 18:37:34
 */
public class MapEntryCodec extends Codec {

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

}
