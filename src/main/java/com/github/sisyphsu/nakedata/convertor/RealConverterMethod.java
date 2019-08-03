package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.reflect.XType;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.MethodAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * ConvertMethod, Must match `<S> S convert(T t, Class<S> clz)`
 *
 * @author sulin
 * @since 2019-06-06 12:19:53
 */
@Slf4j
public class RealConverterMethod extends ConverterMethod {

    private Codec codec;
    private boolean hasTypeArg;
    private Converter annotation;
    private Method method;
    private MethodAccessor function;

    private RealConverterMethod(Class<?> srcClass, Class<?> tgtClass) {
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
        if (method.isBridge() || method.isVarArgs() || method.isDefault() || method.isSynthetic()) {
            log.debug("ignore method by flags: {}", method);
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
        result.function = ReflectionFactory.getReflectionFactory().newMethodAccessor(method);
        return result;
    }

    @Override
    public Object convert(Object data, XType tgtType) {
        if (!annotation.nullable() && data == null) {
            return null;
        }
        if (data != null && data.getClass() != this.getSrcClass()) {
            throw new IllegalArgumentException("data type unmatched: " + this.getSrcClass() + ", " + data.getClass());
        }
        try {
            if (hasTypeArg) {
                return function.invoke(codec, new Object[]{data, tgtType});
            } else {
                return function.invoke(codec, new Object[]{data});
            }
        } catch (InvocationTargetException e) {
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
}
