package com.github.sisyphsu.canoe.convertor;

import com.github.sisyphsu.canoe.reflect.XType;

import java.lang.reflect.Type;

/**
 * Codec's base class.
 *
 * @author sulin
 * @since 2019-05-12 16:01:19
 */
public abstract class Codec {

    private CodecFactory factory;

    public void setFactory(CodecFactory factory) {
        this.factory = factory;
    }

    /**
     * Factory's doConvert convience.
     *
     * @param src     Source Data
     * @param tgtType Target Type
     * @return Target Instance
     */
    public final Object convert(Object src, XType tgtType) {
        return factory.doConvert(src, tgtType);
    }

    /**
     * Factory's doConvert convinence.
     *
     * @param src source data
     * @param clz target class
     * @param <T> Target template type
     * @return target instance
     */
    @SuppressWarnings("unchecked")
    public final <T> T convert(Object src, Class<T> clz) {
        return (T) factory.doConvert(src, toXType(clz));
    }

    public final XType<?> toXType(Type type) {
        return factory.toXType(type);
    }

}
