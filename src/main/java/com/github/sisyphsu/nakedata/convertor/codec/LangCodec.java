package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.Map;

/**
 * Codec for java.lang package, excludes Number and others.
 *
 * @author sulin
 * @since 2019-07-26 10:55:47
 */
public class LangCodec extends Codec {

    /**
     * TODO implementation
     *
     * @param map
     * @param type
     * @return
     */
    public Object toObject(Map map, XType type) {
        if (type.getRawType() == Object.class) {
            return map;
        }
        try {
            Object result = type.getRawType().newInstance();

            return result;
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * Convert String to Enum
     *
     * @param name String
     * @param type Enum Type
     * @return Enum
     */
    @Converter
    @SuppressWarnings("unchecked")
    public Enum toEnum(String name, XType type) {
        return Enum.valueOf(type.getRawType(), name);
    }

    /**
     * Convert Enum to String
     *
     * @param e Enum
     * @return String
     */
    @Converter
    public String toString(Enum e) {
        return e.name();
    }

    /**
     * Convert String to Class
     *
     * @param name String
     * @return Class
     */
    @Converter
    public Class toClass(String name) throws ClassNotFoundException {
        return Class.forName(name);
    }

    /**
     * Convert Class to String
     *
     * @param cls Class
     * @return String
     */
    @Converter
    public String toString(Class cls) {
        return cls.getName();
    }

}
