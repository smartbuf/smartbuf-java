package com.github.sisyphsu.smartbuf.reflect;

import java.util.Objects;

/**
 * XType represents an clear Java type with raw class and its generic types.
 * <p>
 * There has stop-class concept, which will not be.
 *
 * @author sulin
 * @since 2019-07-15 20:40:47
 */
public final class XType<T> {

    /**
     * Basic Object Type
     */
    Class<T>   rawType;
    /**
     * Component type for Object[], support GenericArrayType
     */
    XType<?>   componentType;
    /**
     * ParameterizedType's name in decleared class, like [K, V] for Map<K, V>.
     */
    String[]   parameteriedNames;
    /**
     * Parsed types from ParameterizedType, like [String, Object] for HashMap<String, Object>
     */
    XType<?>[] parameteriedTypes;

    /**
     * Fields, only for no-stop-class
     */
    XField[] fields;

    protected XType(Class<T> rawType) {
        this.rawType = rawType;
    }

    protected XType(Class<T> rawType, XType<?> componentType) {
        this.rawType = rawType;
        this.componentType = componentType;
    }

    protected XType(Class<T> rawType, String[] parameteriedNames, XType<?>[] parameteriedTypes) {
        if (parameteriedNames.length != parameteriedTypes.length) {
            throw new IllegalArgumentException();
        }
        this.rawType = rawType;
        this.parameteriedNames = parameteriedNames;
        this.parameteriedTypes = parameteriedTypes;
    }

    /**
     * Fetch the parameterized type for common generic type, like Collection's E type
     *
     * @return Generic Type
     */
    public XType<?> getParameterizedType() {
        if (this.parameteriedTypes == null || this.parameteriedTypes.length != 1) {
            throw new RuntimeException("Can't getParameterizedType from " + this.toString());
        }
        return this.parameteriedTypes[0];
    }

    /**
     * Fetch the specified parameterized type for named generic type.
     *
     * @param name Parameterized name
     * @return Parameterized type
     */
    public XType<?> getParameterizedType(String name) {
        if (this.parameteriedNames != null) {
            for (int i = 0; i < this.parameteriedNames.length; i++) {
                if (Objects.equals(this.parameteriedNames[i], name)) {
                    return this.parameteriedTypes[i];
                }
            }
        }
        return null;
    }

    /**
     * Fetch all parameterized type sort by place, like Map's K and V type
     *
     * @return Generic Types
     */
    public XType<?>[] getParameterizedTypes() {
        return this.parameteriedTypes;
    }

    /**
     * Check whether this XType is a pure class or not
     *
     * @return Pure type or not
     */
    public boolean isPure() {
        if (componentType != null) {
            return false;
        }
        return parameteriedNames == null;
    }

    public Class<T> getRawType() {
        return rawType;
    }

    public XField[] getFields() {
        return fields;
    }

    public XField<?> getField(String name) {
        if (fields == null) {
            return null;
        }
        for (XField field : fields) {
            if (Objects.equals(field.getName(), name)) {
                return field;
            }
        }
        return null;
    }

    public XType<?> getComponentType() {
        return componentType;
    }

    @Override
    public String toString() {
        // T[]
        if (componentType != null) {
            return componentType + "[]";
        }

        // X<T>
        if (parameteriedTypes != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(rawType.getName());
            sb.append("<");
            for (int i = 0; i < parameteriedTypes.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(parameteriedTypes[i].toString());
            }
            sb.append(">");
            return sb.toString();
        }

        // Object
        return rawType.getName();
    }

}
