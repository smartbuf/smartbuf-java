package com.github.sisyphsu.smartbuf.converter;

import com.github.sisyphsu.smartbuf.reflect.XType;

import java.lang.reflect.Type;

/**
 * Codec's base class.
 *
 * @author sulin
 * @since 2019-05-12 16:01:19
 */
public abstract class Codec {

    private CodecFactory factory;

    /**
     * Setup {@link CodecFactory} which owns this codec instance.
     *
     * @param factory The CodecFactory owns this
     */
    public void setFactory(CodecFactory factory) {
        this.factory = factory;
    }

    /**
     * Proxy of {@link CodecFactory#toXType(Type)}
     */
    protected final XType<?> toXType(Type type) {
        return factory.toXType(type);
    }

    /**
     * Proxy of {@link CodecFactory#doConvert(Object, XType)}
     */
    protected final Object convert(Object src, XType tgtType) {
        return factory.doConvert(src, tgtType);
    }

    /**
     * Proxy of {@link CodecFactory#doConvert(Object, XType)}
     */
    @SuppressWarnings("unchecked")
    protected final <T> T convert(Object src, Class<T> clz) {
        return (T) factory.doConvert(src, toXType(clz));
    }

}
