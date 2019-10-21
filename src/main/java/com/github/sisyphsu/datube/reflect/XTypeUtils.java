package com.github.sisyphsu.datube.reflect;

import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is a convenience utils for XTypeFactory, use common Classes as Stop-Classes by default.
 *
 * @author sulin
 * @since 2019-07-22 14:46:05
 */
public final class XTypeUtils {

    // Global default XTypeFactory
    private static final XTypeFactory factory;

    static {
        factory = new XTypeFactory(Arrays.asList(
                Boolean.class,
                Number.class,
                Character.class,
                Object[].class,
                Collection.class,
                Map.class,
                Map.Entry.class,
                Throwable.class,
                ByteBuffer.class,
                Charset.class,
                CharSequence.class,
                InputStream.class,
                Date.class,
                Reference.class, // TODO why stackoverflow
                AtomicReference.class
        ));
    }

    private XTypeUtils() {
    }

    /**
     * Convert Type to XType, all generic types should be resolved.
     *
     * @param type Standard type like Class/ParameterizedType/GenericArrayType/etc...
     * @return XType instance
     */
    public static XType<?> toXType(Type type) {
        return factory.toXType(type);
    }

}
