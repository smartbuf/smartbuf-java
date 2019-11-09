package com.github.sisyphsu.canoe;

import com.github.sisyphsu.canoe.converter.CodecFactory;
import com.github.sisyphsu.canoe.converter.ConverterPipeline;
import com.github.sisyphsu.canoe.node.NodeCodec;
import com.github.sisyphsu.canoe.reflect.TypeRef;
import com.github.sisyphsu.canoe.reflect.XType;

import java.lang.reflect.Type;

/**
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
     * @return instance of the specified type
     */
    @SuppressWarnings("ALL")
    public static <T> T convert(Object src, TypeRef<T> type) {
        return (T) factory.convert(src, type.getType());
    }

    public static XType<?> toXType(Type type) {
        return factory.toXType(type);
    }

    public static ConverterPipeline getPipeline(Class srcClass, Class tgtClass) {
        return factory.getPipeline(srcClass, tgtClass);
    }
}
