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

    /**
     * Fetch the parameterized type for common generic type, like Collection's E type
     *
     * @return Generic Type
     */
    public XType<?> getParameterizedType() {
        Map.Entry<String, XType> entry = this.parameterizedTypeMap.entrySet().iterator().next();
        return entry.getValue();
    }

    /**
     * Fetch all parameterized type sort by place, like Map's K and V type
     *
     * @return Generic Types
     */
    public XType<?>[] getParameterizedTypes() {
        XType<?>[] result = new XType[parameterizedTypeMap.size()];
        int i = 0;
        for (Map.Entry<String, XType> e : this.parameterizedTypeMap.entrySet()) {
            result[i] = e.getValue();
            i++;
        }
        return result;
    }

    /**
     * Check whether this XType is a pure class or not
     *
     * @return Pure type or not
     */
    public boolean isPure() {
        boolean noBodyGeneric = componentType == null;
        boolean noParamGeneric = parameterizedTypeMap == null || parameterizedTypeMap.isEmpty();
        return noBodyGeneric && noParamGeneric;
    }

}
