package com.github.sisyphsu.nakedata.convertor.codec.map;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.reflect.Type;
import java.util.AbstractMap;
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
    public <T extends Map.Entry> T toMapEntry(Map.Entry entry, Type type) {
        if (entry == null)
            return null;
        Class clz = (Class) type;
        Map.Entry result;
        if (AbstractMap.SimpleEntry.class.isAssignableFrom(clz)) {
            result = new AbstractMap.SimpleEntry(null, null);
        } else if (AbstractMap.SimpleImmutableEntry.class.isAssignableFrom(clz)) {
            result = new AbstractMap.SimpleImmutableEntry(null, null);
        } else {
            throw new RuntimeException("");
        }
        return (T) result;
    }

    public Map.Entry toMapEntry(Map map, Type type) {
        // TODO 将map转换为entry，然后再转换为entry<?,?>
        return null;
    }

    public Map toMap(Map.Entry entry, Type type) {
        // TODO wrap entry as map
        return null;
    }

}
