package com.github.smartbuf.utils;

import com.github.smartbuf.converter.Codec;
import com.github.smartbuf.converter.CodecFactory;
import com.github.smartbuf.converter.ConverterPipeline;
import com.github.smartbuf.node.NodeCodec;
import com.github.smartbuf.reflect.TypeRef;
import com.github.smartbuf.reflect.XType;

import java.lang.reflect.Type;

/**
 * CodecUtils wraps an global {@link CodecFactory} and provides some useful features.
 *
 * @author sulin
 * @since 2019-11-09 20:19:49
 */
public final class CodecUtils {

    private static final CodecFactory factory = new CodecFactory();

    static {
        factory.installCodec(NodeCodec.class);
    }

    private CodecUtils() {
    }

    /**
     * Install new {@link Codec} into the default CodecFactory
     *
     * @param codec The new codec implementation
     */
    public static void installCodec(Codec codec) {
        factory.installCodec(codec);
    }

    /**
     * Convert the specified object to instance of the specified class
     *
     * @param src source data
     * @param clz target class
     * @param <T> Target template type
     * @return instance of the specified class
     */
    public static <T> T convert(Object src, Class<T> clz) {
        return factory.convert(src, clz);
    }

    /**
     * Convert the specified object to the specified type
     *
     * @param src  Source data
     * @param type Target type
     * @param <T>  Target's real type
     * @return instance of the specified type
     */
    @SuppressWarnings("ALL")
    public static <T> T convert(Object src, TypeRef<T> type) {
        return (T) factory.convert(src, type.getType());
    }

    /**
     * Convert the specified {@link Type} into {@link XType}
     *
     * @param type original type
     * @return type's XType instance
     */
    public static XType<?> toXType(Type type) {
        return factory.toXType(type);
    }

    /**
     * Get a pipeline which could convert srcClass's instance into tgtClass's instance
     *
     * @param srcClass Source Class
     * @param tgtClass Target Class
     * @return The final pipeline
     */
    public static ConverterPipeline getPipeline(Class srcClass, Class tgtClass) {
        return factory.getPipeline(srcClass, tgtClass);
    }

}
