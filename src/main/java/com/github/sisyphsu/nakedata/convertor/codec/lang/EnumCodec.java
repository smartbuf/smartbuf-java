package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

/**
 * Enum's codec
 *
 * @author sulin
 * @since 2019-05-13 20:36:53
 */
public class EnumCodec extends Codec {

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
        if (name == null) {
            return null;
        }
        return Enum.valueOf(type.getRawType(), name);
    }

    /**
     * Convert Enum to String
     *
     * @param e Enum
     * @return String
     */
    public String toString(Enum e) {
        return e == null ? null : e.name();
    }

}
