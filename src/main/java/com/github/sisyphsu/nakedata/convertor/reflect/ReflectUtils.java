package com.github.sisyphsu.nakedata.convertor.reflect;

import java.io.InputStream;
import java.lang.reflect.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

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
            AtomicReference.class,
    };

    /**
     * Convert Type to XType, all generic types should be resolved.
     *
     * @param type reflect type
     * @return XType
     */
    public static XType toXType(Type type) {
        // TODO avoid loop
        return toXType(null, type);
    }

    /**
     * Convert Type to XType, resolve all generic types.
     *
     * @param owner Owner Type, help detect generic type
     * @param type  Target which need be resolved
     */
    protected static XType toXType(XType owner, Type type) {
        XType xType;
        if (type instanceof ParameterizedType) {
            xType = convertParameterizedType(owner, (ParameterizedType) type);
        } else if (type instanceof GenericArrayType) {
            xType = convertGenericArrayType(owner, (GenericArrayType) type);
        } else if (type instanceof WildcardType) {
            xType = convertWildcardType(owner, (WildcardType) type);
        } else if (type instanceof TypeVariable) {
            xType = convertTypeVariable(owner, (TypeVariable) type);
        } else if (type instanceof Class) {
            xType = convertClass((Class<?>) type);
        } else {
            throw new IllegalArgumentException("Unsupport Type: " + type);
        }
        return xType;
    }

    /**
     * Convert ParameterizedType to XType
     */
    private static XType convertParameterizedType(XType owner, ParameterizedType type) {
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
            TypeVariable var = variables[i]; // TODO bounds
            Type argType = argTypes[i];
            Type[] bounds = var.getBounds();
            XType argXType = toXType(owner, argType); // should add TypeVariable as 3rd param
            // If argType is TypeVariable, use bounds?
            if (argXType.getRawType() == Object.class && bounds != null && bounds.length == 1) {
                argXType = toXType(owner, bounds[0]);
            }
            String varName = var.getName();
            if (varName.equals("?")) {
                varName = "?" + i;
            }
            parameterizedTypeMap.put(varName, argXType);
        }
        XType result = new XType(rawType, parameterizedTypeMap);
        parseFields(result);
        return result;
    }

    /**
     * Convert GenericArrayType to XType
     */
    private static XType convertGenericArrayType(XType owner, GenericArrayType type) {
        Class<?> rawClass = Object[].class;
        XType xType = toXType(owner, type.getGenericComponentType());
        XType result = new XType(rawClass, xType);
        parseFields(result);
        return result;
    }

    /**
     * Convert WildcardType to XType
     */
    private static XType convertWildcardType(XType owner, WildcardType type) {
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
    private static XType convertTypeVariable(XType owner, TypeVariable type) {
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
     * Convert Class to XType
     */
    private static XType convertClass(Class<?> cls) {
        TypeVariable[] vars = cls.getTypeParameters();
        XType xt;
        if (vars != null && vars.length > 0) {
            // Class supports generic type, but caller didn't use it, like `List l`
            Map<String, XType> paramTypes = new HashMap<>();
            for (int i = 0; i < vars.length; i++) {
                TypeVariable var = vars[i];
                String name = var.getName();
                if (name.equals("?")) {
                    name = "?" + i;
                }
                paramTypes.put(name, convertTypeVariable(null, var)); // no owner
            }
            xt = new XType(cls, paramTypes);
        } else {
            // Class dont support generic type
            xt = new XType(cls);
        }
        parseFields(xt);
        return xt;
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
            if (Modifier.isStatic(field.getModifiers())) {
                continue; // ignore static
            }
            XField xField = new XField();
            xField.setName(field.getName());
            xField.setType(toXType(type, field.getGenericType()));
            xField.setField(field);
            fields.add(xField);
        }
        type.setFields(fields);
    }

}
