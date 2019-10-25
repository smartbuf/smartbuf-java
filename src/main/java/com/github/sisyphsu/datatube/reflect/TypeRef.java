package com.github.sisyphsu.datatube.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Comment copied from `fastjson`:
 * <p>
 * Represents a generic type {@code T}. Java doesn't yet provide a way to
 * represent generic types, so this class does. Forces clients to create a
 * subclass of this class which enables retrieval the type information even at
 * runtime.
 *
 * @author sulin
 * @since 2019-07-22 15:38:53
 */
public abstract class TypeRef<T> {

    private final Type type;

    /**
     * Constructs a new type literal. Derives represented class from type parameter.
     * <p>
     * If caller didnt specify generic type, throw exception directly.
     */
    protected TypeRef() {
        Type superClass = getClass().getGenericSuperclass();
        if (superClass instanceof ParameterizedType) {
            type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
        } else {
            throw new IllegalArgumentException("Must specify TypeRef's parameterized type");
        }
    }

    /**
     * Gets underlying {@code Type} instance.
     */
    public Type getType() {
        return type;
    }

}
