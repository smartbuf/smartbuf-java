package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import sun.reflect.MethodAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Codec的解码(反序列化)方法封装，即Codec的[from*]方法
 *
 * @author sulin
 * @since 2019-05-12 15:41:15
 */
public class DecodeMethod implements CodecMethod {

    private final Codec codec;
    private final Class srcClass;
    private final Class tgtClass;
    private final MethodAccessor accessor;

    /**
     * 根据Codec及其encode或decode方法构建Step
     *
     * @param codec  编码解码器
     * @param method 编码or解码方法
     */
    public DecodeMethod(Codec codec, Method method) {
        Class<?>[] argTypes = method.getParameterTypes();
        if (argTypes.length != 2 || argTypes[0] != Type.class) {
            throw new IllegalArgumentException("invalid codec method: " + method);
        }
        this.codec = codec;
        this.srcClass = argTypes[1];
        this.tgtClass = method.getReturnType();
        this.accessor = ReflectionFactory.getReflectionFactory().getMethodAccessor(method);
    }

    /**
     * 数据Decode函数, 将data还原为指定类型的数据
     *
     * @param type 操作相关的数据类型
     * @param data 原始数据
     * @return 目标类型
     */
    public Object decode(Type type, Object data) {
        try {
            return accessor.invoke(codec, new Object[]{type, data});
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
