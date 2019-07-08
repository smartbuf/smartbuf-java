package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.reflect.Type;

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
    public Enum toEnum(String name, Type type) {
        if (name == null)
            return null;
        Class t = (Class) type;
        return Enum.valueOf(t, name);
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
