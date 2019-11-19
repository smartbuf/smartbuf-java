package com.github.smartbuf.reflect;

import com.github.smartbuf.exception.CircleReferenceException;
import com.github.smartbuf.utils.ReflectUtils;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory for XType, implement Type's reflection and GenericType's resolving.
 * For better performance, it will cache XType for all Type, which shouldn't too many.
 *
 * @author sulin
 * @since 2019-07-22 14:43:00
 */
public final class XTypeFactory {

    /**
     * Stop-Class means some indivisible classes which shouldn't be split
     */
    private Set<Class>          stopClasses = new HashSet<>();
    /**
     * XType's global cache, for performance optimization
     */
    private Map<Type, XType<?>> cacheMap    = new ConcurrentHashMap<>();

    public synchronized XTypeFactory addStopClass(Class<?>... classes) {
        this.stopClasses.addAll(Arrays.asList(classes));
        this.cacheMap.clear();
        return this;
    }

    /**
     * Convert Type to XType, all generic types should be resolved.
     *
     * @param type reflect type
     * @return XType
     */
    public XType<?> toXType(Type type) {
        XType<?> result = cacheMap.get(type);
        if (result == null) {
            Context cxt = new Context(type);
            result = toXType(cxt, null, type);
            cacheMap.put(type, result);
        }
        return result;
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
    private XType<?> toXType(Context cxt, XType<?> owner, Type type) {
        if (cxt.typeMap.containsKey(type)) {
            return cxt.typeMap.get(type); // support circular reference
        }
        XType<?> xType;
        if (type instanceof ParameterizedType) {
            xType = convertParameterizedType(cxt, owner, (ParameterizedType) type);
        } else if (type instanceof GenericArrayType) {
            xType = convertGenericArrayType(cxt, owner, (GenericArrayType) type);
        } else if (type instanceof WildcardType) {
            xType = convertWildcardType(cxt, owner, (WildcardType) type);
        } else if (type instanceof TypeVariable) {
            xType = convertTypeVariable(cxt, owner, (TypeVariable) type);
        } else if (type instanceof Class) {
            xType = convertClass(cxt, (Class<?>) type);
        } else {
            throw new UnsupportedOperationException("Unsupport Type: " + type);
        }
        return xType;
    }

    /**
     * Convert ParameterizedType to XType
     */
    private XType<?> convertParameterizedType(Context cxt, XType<?> owner, final ParameterizedType type) {
        if (!(type.getRawType() instanceof Class)) {
            throw new IllegalArgumentException("Cant parse rawType from " + type); // no way
        }
        Class<?> rawType = (Class) type.getRawType();
        // Parse parameterized types
        Type[] argTypes = type.getActualTypeArguments();
        TypeVariable<?>[] variables = rawType.getTypeParameters();
        if (argTypes.length != variables.length) {
            throw new IllegalStateException("Cant parse ParameterizedType " + type); // no way
        }
        String[] paramTypeNames = new String[variables.length];
        XType<?>[] paramTypes = new XType[variables.length];
        for (int i = 0; i < argTypes.length; i++) {
            TypeVariable var = variables[i];
            // XType from class declared, like `class Bean<T extends Number>{}`
            XType<?> boundXType = toXType(cxt, null, var);
            // XType from field described, like `private Bean<?> bean`
            XType<?> argXType = toXType(cxt, owner, argTypes[i]);
            XType<?> finalXType;
            // Don't support combined generic-type, adopt genericType from Type or declaredType from Class
            if (argXType.getRawType().isAssignableFrom(boundXType.getRawType())) {
                finalXType = boundXType;
            } else {
                finalXType = argXType;
            }
            paramTypeNames[i] = var.getName();
            paramTypes[i] = finalXType;
        }
        XType<?> result = new XType<>(type, rawType, paramTypeNames, paramTypes);

        cxt.typeMap.put(type, result);
        this.parseFields(cxt, result);
        return result;
    }

    /**
     * Convert GenericArrayType to XType
     */
    private XType<?> convertGenericArrayType(Context cxt, XType<?> owner, final GenericArrayType type) {
        Class<?> rawClass = Object[].class;
        XType<?> componentXType = toXType(cxt, owner, type.getGenericComponentType());
        XType<?> result = new XType<>(type, rawClass, componentXType);

        cxt.typeMap.put(type, result);
        parseFields(cxt, result);
        return result;
    }

    /**
     * Convert WildcardType to XType
     */
    private XType<?> convertWildcardType(Context cxt, XType<?> owner, WildcardType type) {
        Type[] uppers = type.getUpperBounds();
        Type[] lowers = type.getLowerBounds();
        if (lowers.length == 1) {
            return toXType(cxt, owner, lowers[0]); // treat <? super T> as <T>
        }
        if (uppers.length == 1) {
            return toXType(cxt, owner, uppers[0]); // treat <? extends T> as <T>
        }
        throw new IllegalArgumentException("unresolved WildcardType: " + type);
    }

    /**
     * Convert TypeVariable to XType
     */
    private XType<?> convertTypeVariable(Context cxt, XType<?> owner, TypeVariable type) {
        if (owner != null) {
            String varName = type.getName();
            XType<?> xType = owner.getParameterizedType(varName);
            if (xType == null) {
                throw new IllegalArgumentException("unresolved type for TypeVariable " + type);
            }
            return xType;
        }
        Type[] bounds = type.getBounds();
        if (bounds.length != 1) {
            throw new IllegalArgumentException("unresolved TypeVariable " + type);
        }
        return toXType(cxt, null, bounds[0]);
    }

    /**
     * Convert Class to XType
     */
    private <T> XType<T> convertClass(Context cxt, final Class<T> cls) {
        XType<T> result;
        TypeVariable[] vars = cls.getTypeParameters();
        if (vars.length > 0) {
            // Class supports generic type, but caller didn't use it, like `List l`
            String[] paramTypeNames = new String[vars.length];
            XType<?>[] paramTypes = new XType[vars.length];
            for (int i = 0; i < vars.length; i++) {
                TypeVariable var = vars[i];
                paramTypeNames[i] = var.getName();
                paramTypes[i] = convertTypeVariable(cxt, null, var);// no owner
            }
            result = new XType<>(cls, cls, paramTypeNames, paramTypes);
        } else {
            // Class dont support generic type
            result = new XType<>(cls, cls);
        }

        cxt.typeMap.put(cls, result);
        parseFields(cxt, result);
        return result;
    }

    /**
     * Parse fields of XType and fill them
     */
    @SuppressWarnings("unchecked")
    private void parseFields(Context cxt, XType<?> type) {
        Class rawCls = type.getRawType();
        if (rawCls == Object.class || rawCls.isPrimitive() || rawCls.isArray() || rawCls.isEnum()) {
            return;
        }
        // If rawCls is a stop-class, return directly
        for (Class<?> stopType : this.stopClasses) {
            if (stopType.isAssignableFrom(type.getRawType())) {
                return;
            }
        }
        // Precheck to prevent circular reference
        if (cxt.parsing.contains(rawCls)) {
            throw new CircleReferenceException("Circular Reference in: " + rawCls);
        }
        cxt.parsing.add(rawCls);
        // execute fields parsing
        Map<String, XField> fields = new TreeMap<>();
        ReflectUtils.findValidFields(rawCls).forEach(field -> {
            XType fieldType = toXType(cxt, type, field.getGenericType());
            XField<?> xField = new XField<>();
            xField.setName(field.getName());
            xField.setType(fieldType);
            xField.setField(field);
            fields.put(xField.getName(), xField);
        });
        // collect super->fields if it exists
        if (rawCls.getGenericSuperclass() != null) {
            XType<?> superType = toXType(cxt, null, rawCls.getGenericSuperclass());
            if (superType.fields != null) {
                for (XField field : superType.fields) {
                    if (fields.containsKey(field.getName())) {
                        continue; // ignore covered
                    }
                    fields.put(field.getName(), field);
                }
            }
        }
        cxt.parsing.remove(rawCls);
        type.fields = fields.values().toArray(new XField[0]);
    }

    /**
     * Context is responsible for recording type-convert status
     */
    static class Context {
        Type             root;
        Set<Class>       parsing = new HashSet<>();
        Map<Type, XType> typeMap = new HashMap<>();

        Context(Type root) {
            this.root = root;
        }
    }

}
