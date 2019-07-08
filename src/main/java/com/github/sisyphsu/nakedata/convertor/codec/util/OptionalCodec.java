package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Optional's codec
 *
 * @author sulin
 * @since 2019-05-13 18:15:18
 */
public class OptionalCodec extends Codec {

    /**
     * Convert Object to Optional
     *
     * @param o    Object
     * @param type Type
     * @return Optional
     */
    public Optional toOptional(Object o, Type type) {
        if (o == null)
            return Optional.empty();

        Type genericType = getGenericType(type);
        if (genericType == null) {
            return Optional.of(o);
        } else {
            return Optional.of(convert(o, genericType));
        }
    }

    /**
     * Convert Optional to Object
     *
     * @param optional Optional
     * @return Object
     */
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public Object toObject(Optional optional) {
        if (!optional.isPresent())
            return null;
        return optional.get();
    }

}
