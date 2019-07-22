package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

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
    public Optional toOptional(Object o, XType type) {
        if (o == null)
            return Optional.empty();

        XType<?> genericType = type.getParameterizedType();
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
