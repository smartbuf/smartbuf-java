package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.Codec;
import com.github.sisyphsu.smartbuf.converter.Converter;
import com.github.sisyphsu.smartbuf.reflect.XType;

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
public final class MapCodec extends Codec {

    /**
     * Convert Map to Map with the specified generic type
     */
    @Converter(extensible = true)
    public Map toMap(Map<?, ?> map, XType<?> type) {
        if (map.isEmpty() && type.getRawType().isInstance(map)) {
            return map;
        }
        // check compatible, avoid unnessery copy
        Class<?> rawType = type.getRawType();
        XType<?>[] paramTypes = type.getParameterizedTypes();
        XType<?> keyType = paramTypes[0];
        XType<?> valType = paramTypes[1];
        Class<?> keyRawType = keyType.getRawType();
        Class<?> valRawType = valType.getRawType();
        boolean compatible = rawType.isInstance(map) && keyType.isPure() && valType.isPure();
        if (compatible && !(keyRawType == Object.class && valRawType == Object.class)) {
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object key = entry.getKey();
                Object val = entry.getValue();
                if (!(compatible = keyRawType.isInstance(key) && valRawType.isInstance(val))) {
                    break;
                }
            }
        }
        if (compatible) {
            return map;
        }
        // Build new Map
        Map result = create(rawType, keyType.getRawType(), map.size());
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            Object key = convert(entry.getKey(), keyType);
            Object val = convert(entry.getValue(), valType);
            result.put(key, val);
        }
        return result;
    }

    /**
     * Convert Map.Entry to Map, dont handle generic type
     */
    @Converter
    public Map toMap(Map.Entry entry) {
        Map map = new HashMap();
        map.put(entry.getKey(), entry.getValue());
        return map;
    }

    /**
     * Convert Map to Map.Entry, dont handle generic type
     */
    @Converter
    public Map.Entry toMapEntry(Map<?, ?> map) {
        if (map.isEmpty())
            return null;
        if (map.size() > 1) {
            throw new IllegalArgumentException("Can't convert Map[size > 1] to Map.Entry");
        }
        return map.entrySet().iterator().next();
    }

    /**
     * Convert Map.Entry to Map.Entry with the specified generic type
     */
    @Converter(extensible = true)
    public Map.Entry toMapEntry(Map.Entry<?, ?> entry, XType<?> type) {
        Class<?> clz = type.getRawType();
        XType<?>[] paramTypes = type.getParameterizedTypes();
        Object key = convert(entry.getKey(), paramTypes[0]);
        Object val = convert(entry.getValue(), paramTypes[1]);
        if (clz.isAssignableFrom(AbstractMap.SimpleEntry.class))
            return new AbstractMap.SimpleEntry(key, val);
        if (clz.isAssignableFrom(AbstractMap.SimpleImmutableEntry.class))
            return new AbstractMap.SimpleImmutableEntry(key, val);
        throw new IllegalArgumentException("Unsupported Type: " + type.getRawType());
    }

    /**
     * Create an Map instance by the specified Class
     *
     * @param clz     The specified Map class
     * @param keyType Key Type
     * @param size    initial size
     * @return Map Instance
     */
    public static Map create(Class<?> clz, Class<?> keyType, int size) {
        if (clz.isAssignableFrom(HashMap.class))
            return new HashMap();
        if (clz.isAssignableFrom(TreeMap.class))
            return new TreeMap();
        if (clz.isAssignableFrom(Hashtable.class))
            return new Hashtable();
        if (clz.isAssignableFrom(WeakHashMap.class))
            return new WeakHashMap(size);
        if (clz.isAssignableFrom(LinkedHashMap.class))
            return new LinkedHashMap();
        if (clz.isAssignableFrom(IdentityHashMap.class))
            return new IdentityHashMap();
        if (clz.isAssignableFrom(ConcurrentHashMap.class))
            return new ConcurrentHashMap(size);
        if (clz.isAssignableFrom(ConcurrentSkipListMap.class))
            return new ConcurrentSkipListMap();
        if (clz.isAssignableFrom(Properties.class))
            return new Properties();
        if (clz.isAssignableFrom(Attributes.class))
            return new Attributes(size);
        if (clz.isAssignableFrom(EnumMap.class))
            return new EnumMap(keyType);
        throw new UnsupportedOperationException("Unsupport Map: " + clz);
    }

}
