package com.github.sisyphsu.nakedata.convertor.codec.map;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.reflect.Type;
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
    public <T extends Map> T toMap(Map map, Type type) {
        if (map == null)
            return null;
        Class clz = (Class) type;
        Map result;
        if (EnumMap.class.isAssignableFrom(clz)) {
            result = new EnumMap(clz); // TODO 取
        } else if (HashMap.class.isAssignableFrom(clz)) {
            result = new HashMap();
        } else if (Hashtable.class.isAssignableFrom(clz)) {
            result = new Hashtable();
        } else if (IdentityHashMap.class.isAssignableFrom(clz)) {
            result = new IdentityHashMap();
        } else if (LinkedHashMap.class.isAssignableFrom(clz)) {
            result = new LinkedHashMap();
        } else if (Properties.class.isAssignableFrom(clz)) {
            result = new Properties();
        } else if (TreeMap.class.isAssignableFrom(clz)) {
            result = new TreeMap();
        } else if (WeakHashMap.class.isAssignableFrom(clz)) {
            result = new WeakHashMap(map.size());
        } else if (ConcurrentHashMap.class.isAssignableFrom(clz)) {
            result = new ConcurrentHashMap(map.size());
        } else if (ConcurrentSkipListMap.class.isAssignableFrom(clz)) {
            result = new ConcurrentSkipListMap();
        } else if (Attributes.class.isAssignableFrom(clz)) {
            result = new Attributes(map.size());
        } else {
            throw new RuntimeException("");
        }
        return (T) result;
    }

}
