package com.github.sisyphsu.nakedata.convertor.reflect;

import lombok.Data;

import java.util.Map;

/**
 * XType represent an clear Java type with raw class and its generic types.
 * <p>
 * There has stop-class concept, which will not be.
 *
 * @author sulin
 * @since 2019-07-15 20:40:47
 */
@Data
public class XType<T> {

    /**
     * Basic Object Type
     */
    private Class<T> rawType;
    /**
     * Component type for Object[], support GenericArrayType
     */
    private XType<?> componentType;
    /**
     * Parsed type map for ParameterizedType
     */
    private Map<String, XType> parameterizedTypeMap;
    /**
     * Fields, only for no-stop-class
     */
    private Map<String, XField> fields;

    public XType(Class<T> rawType) {
        this.rawType = rawType;
    }

    public XType(Class<T> rawType, XType<?> componentType) {
        this.rawType = rawType;
        this.componentType = componentType;
    }

    public XType(Class<T> rawType, Map<String, XType> parameterizedTypeMap) {
        this.rawType = rawType;
        this.parameterizedTypeMap = parameterizedTypeMap;
    }

    public XType<?> getParameterizedType() {
        Map.Entry<String, XType> entry = this.parameterizedTypeMap.entrySet().iterator().next();
        return entry.getValue();
    }

}
