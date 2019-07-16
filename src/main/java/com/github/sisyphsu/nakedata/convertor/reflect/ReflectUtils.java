package com.github.sisyphsu.nakedata.convertor.reflect;

import java.io.InputStream;
import java.lang.reflect.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Reflect utils
 *
 * @author sulin
 * @since 2019-07-15 20:56:55
 */
public class ReflectUtils {

    private static final Class[] STOP_TYPES = new Class[]{
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
    };

    /**
     * Convert Type to XType, all generic types should be resolved.
     *
     * @param type reflect type
     * @return XType
     */
    public static XType toXType(Type type) {
        return toXType(null, type);
    }

    /**
     * Convert Type to XType, resolve all generic types.
     */
    private static XType toXType(XType owner, Type type) {
        XType xType;
        if (type instanceof ParameterizedType) {
            xType = toXType(owner, (ParameterizedType) type);
        } else if (type instanceof GenericArrayType) {
            xType = toXType(owner, (GenericArrayType) type);
        } else if (type instanceof WildcardType) {
            xType = toXType(owner, (WildcardType) type);
        } else if (type instanceof TypeVariable) {
            xType = toXType(owner, (TypeVariable) type);
        } else if (type instanceof Class) {
            xType = new XType((Class<?>) type);
        } else {
            throw new IllegalArgumentException("Unsupport Type: " + type);
        }
        parseFields(xType);

        return xType;
    }

    /**
     * Convert ParameterizedType to XType
     */
    private static XType toXType(XType owner, ParameterizedType type) {
        if (!(type.getRawType() instanceof Class)) {
            throw new IllegalArgumentException("Cant parse rawType from " + type); // no way
        }
        Class rawType = (Class) type.getRawType();
        // Parse parameterized types
        Map<String, XType> parameterizedTypeMap = new HashMap<>();
        Type[] argTypes = type.getActualTypeArguments();
        TypeVariable<?>[] variables = rawType.getTypeParameters();
        if (argTypes == null || variables == null || argTypes.length != variables.length) {
            throw new IllegalStateException("Cant parse ParameterizedType " + type); // no way
        }
        for (int i = 0; i < argTypes.length; i++) {
            Type argType = argTypes[i];
            TypeVariable var = variables[i];
            XType argXType = toXType(owner, argType); // argType could be TypeVariable from owner
            parameterizedTypeMap.put(var.getName(), argXType);
        }
        return new XType(rawType, parameterizedTypeMap);
    }

    /**
     * Convert GenericArrayType to XType
     */
    private static XType toXType(XType owner, GenericArrayType type) {
        Class<?> rawClass = Object[].class;
        XType xType = toXType(owner, type.getGenericComponentType());
        return new XType(rawClass, xType);
    }

    /**
     * Convert WildcardType to XType
     */
    private static XType toXType(XType owner, WildcardType type) {
        Type[] uppers = type.getUpperBounds();
        Type[] lowers = type.getLowerBounds();
        if (uppers != null && uppers.length == 1) {
            return toXType(owner, uppers[0]); // treat <? extends T> as <T>
        }
        if (lowers != null && lowers.length == 1) {
            return toXType(owner, lowers[0]); //treat <? super T> as <T>
        }
        throw new IllegalArgumentException("unresolved WildcardType: " + type);
    }

    /**
     * Convert TypeVariable to XType
     */
    private static XType toXType(XType owner, TypeVariable type) {
        String varName = type.getName();
        if (owner != null && owner.getParameterizedTypeMap() != null) {
            if (owner.getParameterizedTypeMap() == null) {
                throw new IllegalArgumentException("unresolved owner for TypeVariable " + type);
            }
            XType xType = owner.getParameterizedTypeMap().get(varName);
            if (xType == null) {
                throw new IllegalArgumentException("unresolved owner for TypeVariable " + type);
            }
            return xType;
        }
        Type[] bounds = type.getBounds();
        if (bounds == null || bounds.length != 1) {
            throw new IllegalArgumentException("unresolved TypeVariable " + type);
        }
        return toXType(null, bounds[0]);
    }

    /**
     * Parse fields of XType and fill them
     */
    private static void parseFields(XType type) {
        for (Class<?> stopType : STOP_TYPES) {
            if (stopType.isAssignableFrom(type.getRawType())) {
                return; // type is stop class like Number/Collection...
            }
        }
        List<XField> fields = new ArrayList<>();
        for (Field field : type.getRawType().getDeclaredFields()) {
            XField xField = new XField();
            xField.setName(field.getName());
            xField.setType(toXType(type, field.getGenericType()));
            xField.setField(field);
            fields.add(xField);
        }
        type.setFields(fields);
    }

}
