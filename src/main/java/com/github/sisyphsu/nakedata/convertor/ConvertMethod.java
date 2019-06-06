package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import lombok.extern.slf4j.Slf4j;
import sun.reflect.MethodAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * ConvertMethod, Must match `<S> S convert(T t, Class<S> clz)`
 *
 * @author sulin
 * @since 2019-06-06 12:19:53
 */
@Slf4j
public class ConvertMethod {

    private Codec codec;
    private Class srcClass;
    private Class tgtClass;
    private boolean hasTypeArg;
    private MethodAccessor function;

    private ConvertMethod() {
    }

    public static ConvertMethod valueOf(Codec codec, Method method) {
        Class<?>[] argTypes = method.getParameterTypes();
        Class<?> rtType = method.getReturnType();
        if (method.isBridge() || method.isVarArgs() || method.isDefault() || method.isSynthetic()) {
            log.debug("ignore method by flags: {}", method);
            return null;
        }
        if (rtType == Void.class) {
            log.debug("ignore method by void return: {}", method);
            return null; // ignore void return
        }
        if (argTypes.length != 1 && argTypes.length != 2) {
            log.debug("ignore method by argument count: {}", method);
            return null;
        }
        if (argTypes.length == 2 && argTypes[1] != Type.class) {
            log.debug("ignore method by the second argument!=Type: {}", method);
            return null;
        }
        ConvertMethod result = new ConvertMethod();
        result.codec = codec;
        result.srcClass = argTypes[0];
        result.tgtClass = rtType;
        result.hasTypeArg = argTypes.length == 2;
        result.function = ReflectionFactory.getReflectionFactory().getMethodAccessor(method);
        return result;
    }

    /**
     * Convert the data to target instance
     *
     * @param data    Source data
     * @param tgtType Target class
     * @return target instance
     */
    public Object convert(Object data, Type tgtType) {
        if (data != null && data.getClass() != this.srcClass) {
            throw new IllegalArgumentException("data type unmatched: " + this.srcClass + ", " + data.getClass());
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

    public Class getSrcClass() {
        return srcClass;
    }

    public Class getTgtClass() {
        return tgtClass;
    }

}
