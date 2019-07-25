package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Optional's codec
 *
 * @author sulin
 * @since 2019-05-13 18:15:18
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OptionalCodec extends Codec {

    /**
     * Convert Object to Optional
     *
     * @param o    Object
     * @param type Type
     * @return Optional
     */
    @Converter
    public Optional toOptional(Object o, XType type) {
        if (o == null)
            return Optional.empty();
        XType<?> genericType = type.getParameterizedType();
        if (genericType.isPure() && genericType.getRawType().isAssignableFrom(o.getClass())) {
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
    @Converter
    public Object toObject(Optional optional) {
        return optional.isPresent() ? optional.get() : null;
    }

    /**
     * Convert Double to OptionalDouble
     *
     * @param d Double
     * @return OptionalDouble
     */
    @Converter
    public OptionalDouble toOptionalDouble(Double d) {
        return d == null ? OptionalDouble.empty() : OptionalDouble.of(d);
    }

    /**
     * Convert OptionalDouble to Double
     *
     * @param od OptionalDouble
     * @return Double
     */
    @Converter
    public Double toDouble(OptionalDouble od) {
        return od.isPresent() ? od.getAsDouble() : null;
    }

    /**
     * Convert Integer to OptionalInt
     *
     * @param i Integer
     * @return OptionalInt
     */
    @Converter
    public OptionalInt toOptionalInt(Integer i) {
        return i == null ? OptionalInt.empty() : OptionalInt.of(i);
    }

    /**
     * Convert OptionalInt to Integer
     *
     * @param oi OptionalInt
     * @return Integer
     */
    @Converter
    public Integer toInteger(OptionalInt oi) {
        return oi.isPresent() ? oi.getAsInt() : null;
    }

    /**
     * Convert Long to OptionalLong
     *
     * @param l Long
     * @return OptionalLong
     */
    @Converter
    public OptionalLong toOptionalLong(Long l) {
        return l == null ? OptionalLong.empty() : OptionalLong.of(l);
    }

    /**
     * Convert OptionalLong to Long
     *
     * @param ol OptionalLong
     * @return Long
     */
    @Converter
    public Long toLong(OptionalLong ol) {
        return ol.isPresent() ? ol.getAsLong() : null;
    }

}
