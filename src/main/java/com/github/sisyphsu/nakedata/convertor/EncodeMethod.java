package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import sun.reflect.MethodAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Codec编码(序列化)方法封装, 即Codec的[to*]方法
 *
 * @author sulin
 * @since 2019-05-20 15:20:45
 */
public class EncodeMethod implements CodecMethod {

    private final Codec codec;
    private final Class srcClass;
    private final Class tgtClass;
    private final MethodAccessor accessor;

    /**
     * 封装指定Codec的encode方法
     *
     * @param codec  编码解码器
     * @param method 编码or解码方法
     */
    public EncodeMethod(Codec codec, Method method) {
        Class<?>[] argTypes = method.getParameterTypes();
        if (argTypes.length != 1 || argTypes[0] != codec.support() || method.getReturnType() == Void.class) {
            throw new IllegalArgumentException("invalid codec method: " + method);
        }
        this.codec = codec;
        this.srcClass = codec.support();
        this.tgtClass = method.getReturnType();
        this.accessor = ReflectionFactory.getReflectionFactory().getMethodAccessor(method);
    }

    /**
     * 数据转换函数, 表示编码or解码环节的两种数据类型的转换规则
     *
     * @param src 原始数据
     * @return 目标类型
     */
    public Object encode(Object src) {
        try {
            return accessor.invoke(codec, new Object[]{src});
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
