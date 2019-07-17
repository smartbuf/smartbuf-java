package com.github.sisyphsu.nakedata.convertor.reflect;

import lombok.Data;

import java.util.List;
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
public class XType {

    /**
     * Basic Object Type
     */
    private Class<?> rawType;
    /**
     * Component type for Object[], support GenericArrayType
     */
    private XType componentType;
    /**
     * Parsed type map for ParameterizedType
     */
    private Map<String, XType> parameterizedTypeMap;
    /**
     * Fields
     */
    private List<XField> fields;

    public XType(Class<?> rawType) {
        this.rawType = rawType;
    }

    public XType(Class<?> rawType, XType componentType) {
        this.rawType = rawType;
        this.componentType = componentType;
    }

    public XType(Class<?> rawType, Map<String, XType> parameterizedTypeMap) {
        this.rawType = rawType;
        this.parameterizedTypeMap = parameterizedTypeMap;
    }

}
