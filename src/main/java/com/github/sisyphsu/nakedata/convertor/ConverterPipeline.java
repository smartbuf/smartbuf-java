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

    private ConverterPipeline(Class srcClass, Class tgtClass, List<ConverterMethod> methods) {
        this.srcClass = srcClass;
        this.tgtClass = tgtClass;
        this.methods = methods;
    }

    public static ConverterPipeline valueOf(Class src, Class tgt) {
        return new ConverterPipeline(src, tgt, null);
    }

    public static ConverterPipeline valueOf(List<ConverterMethod> methods) {
        if (methods == null || methods.size() == 0) {
            throw new IllegalArgumentException("methods can't be null or empty");
        }
        Class srcClass = methods.get(0).getSrcClass();
        Class tgtClass = methods.get(methods.size() - 1).getTgtClass();
        return new ConverterPipeline(srcClass, tgtClass, methods);
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