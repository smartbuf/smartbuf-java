package com.github.sisyphsu.datube.reflect;

import java.lang.ref.Reference;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * This is a convenience utils for XTypeFactory, use common Classes as Stop-Classes by default.
 *
 * @author sulin
 * @since 2019-07-22 14:46:05
 */
public final class XTypeUtils {

    // Global default XTypeFactory
    private static final XTypeFactory factory = new XTypeFactory(Arrays.asList(
        Object[].class,
        Boolean.class,
        Number.class,
        CharSequence.class,
        Collection.class,
        Map.class,
        Reference.class
    ));

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
