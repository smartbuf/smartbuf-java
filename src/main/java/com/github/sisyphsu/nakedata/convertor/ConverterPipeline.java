package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.reflect.XType;

import java.util.List;

/**
 * ConverterPipeline represent an integrated converter pipeline,
 * it holds the full path from srcClass to tgtClass, like:
 * 1. string -> map -> POJO
 * 2. ObjectNode -> map -> POJO
 * 3. ByteNode -> byte[] -> Date
 *
 * @author sulin
 * @since 2019-05-20 15:24:12
 */
public class ConverterPipeline {

    private final Class srcClass;
    private final Class tgtClass;
    private final List<ConverterMethod> methods;

    public ConverterPipeline(Class srcClass, Class tgtClass, List<ConverterMethod> methods) {
        this.srcClass = srcClass;
        this.tgtClass = tgtClass;
        this.methods = methods;
    }

    /**
     * Convert the specified source data to tgtType instance.
     *
     * @param data    Source data
     * @param tgtType Target type, with generic info
     * @return Target, match tgtType
     */
    public Object convert(Object data, XType tgtType) {
        Object result = data;
        for (ConverterMethod method : methods) {
            result = method.convert(result, tgtType);
        }
        return result;
    }

    public Class getSrcClass() {
        return srcClass;
    }

    public Class getTgtClass() {
        return tgtClass;
    }

}