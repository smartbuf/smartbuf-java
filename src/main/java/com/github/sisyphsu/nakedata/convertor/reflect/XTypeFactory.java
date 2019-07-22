package com.github.sisyphsu.nakedata.convertor.reflect;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory for XType, implement Type's reflection and GenericType's resolving.
 * For better performance, It cache XType for all Type, which shouldn't too many.
 *
 * @author sulin
 * @since 2019-07-22 14:43:00
 */
public class XTypeFactory {

    /**
     * Stop-Class means some indivisible classes which shouldn't be split
     */
    private final List<Class> stopClasses;
    /**
     * XType's global cache, for performance optimization
     */
    private final Map<Type, XType> cacheMap;

    /**
     * Initialize XTypeFactory by specified stopClasses
     *
     * @param stopClasses Stop-Classes
     */
    public XTypeFactory(Collection<Class> stopClasses) {
        this.stopClasses = new ArrayList<>(stopClasses);
        this.cacheMap = new ConcurrentHashMap<>();
    }

    /**
     * Convert Type to XType, all generic types should be resolved.
     *
     * @param type reflect type
     * @return XType
     */
    public XType toXType(Type type) {
        return cacheMap.computeIfAbsent(type, (t) -> toXType(null, t));
    }

    /**
     * Convert Type to XType, resolve all generic types.
     * <p>
     * owner's usecase:
     * class Bean<T> {private T t;}
     *
     * @param owner Owner Type, help to decide TypeVariable's real type
     * @param type  Target which need be resolved
     */
    protected XType toXType(XType owner, Type type) {
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
    private XType convertParameterizedType(XType owner, ParameterizedType type) {
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
            TypeVariable var = variables[i];
            // XType from class declared, like `class Bean<T extends Number>{}`
            XType boundXType = toXType(null, var);
            // XType from field described, like `private Bean<?> bean`
            XType argXType = toXType(owner, argTypes[i]);
            XType finalXType;
            // Don't support combined generic-type, adopt genericType from Type or declaredType from Class
            if (argXType.getRawType().isAssignableFrom(boundXType.getRawType())) {
                finalXType = boundXType;
            } else {
                finalXType = argXType;
            }
            String varName = var.getName();
            if (varName.equals("?")) {
                varName = "?" + i;
            }
            parameterizedTypeMap.put(varName, finalXType);
        }
        XType result = new XType(rawType, parameterizedTypeMap);
        parseFields(result);
        return result;
    }

    /**
     * Convert GenericArrayType to XType
     */
    private XType convertGenericArrayType(XType owner, GenericArrayType type) {
        Class<?> rawClass = Object[].class;
        XType xType = toXType(owner, type.getGenericComponentType());
        XType result = new XType(rawClass, xType);
        parseFields(result);
        return result;
    }

    /**
     * Convert WildcardType to XType
     */
    private XType convertWildcardType(XType owner, WildcardType type) {
        Type[] uppers = type.getUpperBounds();
        Type[] lowers = type.getLowerBounds();
        if (lowers != null && lowers.length == 1) {
            return toXType(owner, lowers[0]); //treat <? super T> as <T>
        }
        if (uppers != null && uppers.length == 1) {
            return toXType(owner, uppers[0]); // treat <? extends T> as <T>
        }
        throw new IllegalArgumentException("unresolved WildcardType: " + type);
    }

    /**
     * Convert TypeVariable to XType
     */
    private XType convertTypeVariable(XType owner, TypeVariable type) {
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
    private XType convertClass(Class<?> cls) {
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
    private void parseFields(XType type) {
        for (Class<?> stopType : this.stopClasses) {
            if (stopType.isAssignableFrom(type.getRawType())) {
                return; // type is stop class like Number/Collection...
            }
        }
        Map<String, XField> fields = new HashMap<>();
        for (Field field : type.getRawType().getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue; // ignore static
            }
            XField xField = new XField();
            xField.setName(field.getName());
            xField.setType(toXType(type, field.getGenericType()));
            xField.setField(field);

            fields.put(xField.getName(), xField);
        }
        type.setFields(fields);
    }

}
