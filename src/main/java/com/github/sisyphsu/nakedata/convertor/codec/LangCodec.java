package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.reflect.XField;
import com.github.sisyphsu.nakedata.reflect.XType;
import net.sf.cglib.beans.BeanMap;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Codec for java.lang package, excludes Number and others.
 *
 * @author sulin
 * @since 2019-07-26 10:55:47
 */
@SuppressWarnings("unchecked")
public final class LangCodec extends Codec {

    /**
     * Convert Map to Object
     */
    @Converter(extensible = true, distance = 10000)
    public Object toObject(Map<?, ?> map, XType<?> type) {
        if (type.getRawType() == Object.class) {
            return map; // dont need convert
        }
        Object result;
        try {
            result = type.getRawType().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException("Can't newInstance of " + type.getRawType() + ": ", e);
        }
        BeanMap bm = BeanMap.create(result);
        for (Map.Entry<String, XField> entry : type.getFields().entrySet()) {
            Object data = map.get(entry.getKey());
            if (data == null) {
                continue;
            }
            bm.put(entry.getKey(), convert(data, entry.getValue().getType()));
        }
        return result;
    }

    /**
     * Convert Object to Map, use cglib directly
     */
    @Converter(distance = 10000)
    public Map toMap(Object obj) {
        return BeanMap.create(obj);
    }

    /**
     * Convert String to Enum
     */
    @Converter(extensible = true)
    public Enum toEnum(String name, XType type) {
        return Enum.valueOf(type.getRawType(), name);
    }

    /**
     * Convert Enum to String
     */
    @Converter
    public String toString(Enum e) {
        return e.name();
    }

    /**
     * Convert String to Class
     */
    @Converter
    public Class toClass(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }

    /**
     * Convert Class to String
     */
    @Converter
    public String toString(Class cls) {
        return cls.getName();
    }

    /**
     * Convert String to SimpleDateFormat
     */
    @Converter
    public SimpleDateFormat toSimpleDateFormat(String s) {
        return new SimpleDateFormat(s);
    }

    /**
     * Convert SimpleDateFormat to String
     */
    @Converter
    public String toString(SimpleDateFormat format) {
        return format.toPattern();
    }

}
