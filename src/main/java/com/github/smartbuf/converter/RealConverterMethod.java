package com.github.smartbuf.converter;

import com.github.smartbuf.reflect.XType;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * ConvertMethod, Must match `[S] S convert(T t, Class[S] clz)`
 *
 * @author sulin
 * @since 2019-06-06 12:19:53
 */
@Slf4j
public final class RealConverterMethod extends ConverterMethod {

    protected Codec     codec;
    protected boolean   hasTypeArg;
    protected Converter annotation;
    protected Method    method;

    RealConverterMethod(Class<?> srcClass, Class<?> tgtClass) {
        super(srcClass, tgtClass);
    }

    /**
     * Create RealConverterMethod by Codec and Method
     *
     * @param codec  Codec instance
     * @param method Method reference
     * @return RealConverterMethod
     */
    public static RealConverterMethod valueOf(Codec codec, Method method) {
        Class<?>[] argTypes = method.getParameterTypes();
        Class<?> rtType = method.getReturnType();
        Converter annotation = method.getAnnotation(Converter.class);
        if (annotation == null) {
            log.debug("ignore method that don't have @Converter: {}", method);
            return null;
        }
        if (method.isVarArgs()) {
            log.debug("ignore varargs method: {}", method);
            return null;
        }
        if (rtType == void.class || rtType == Void.class) {
            log.debug("ignore method by void return: {}", method);
            return null; // ignore void return
        }
        if (argTypes.length != 1 && argTypes.length != 2) {
            log.debug("ignore method by argument count: {}", method);
            return null;
        }
        if (argTypes.length == 2 && argTypes[1] != XType.class) {
            log.debug("ignore method by the second argument!=Type: {}", method);
            return null;
        }
        RealConverterMethod result = new RealConverterMethod(argTypes[0], rtType);
        result.annotation = annotation;
        result.method = method;
        result.codec = codec;
        result.hasTypeArg = argTypes.length == 2;
        return result;
    }

    @Override
    public Object convert(Object data, XType tgtType) {
        if (!annotation.nullable() && data == null) {
            return null;
        }
        if (data != null && !this.getSrcClass().isAssignableFrom(data.getClass())) {
            throw new IllegalArgumentException("data type unmatched: " + this.getSrcClass() + ", " + data.getClass());
        }
        try {
            if (hasTypeArg) {
                return method.invoke(codec, data, tgtType);
            } else {
                return method.invoke(codec, data);
            }
        } catch (Exception e) {
            throw new IllegalStateException("invoke codec failed.", e);
        }
    }

    @Override
    public int getDistance() {
        return annotation.distance();
    }

    @Override
    public boolean isExtensible() {
        return annotation.extensible();
    }

    public boolean isHasTypeArg() {
        return hasTypeArg;
    }

    @Override
    public String toString() {
        return String.format("[%s]", method.toString());
    }
}
