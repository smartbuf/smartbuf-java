package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import sun.reflect.MethodAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * @author sulin
 * @since 2019-05-20 15:20:45
 */
public class EncodeStep {

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
    public EncodeStep(Codec codec, Method method) {
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

    /**
     * 获取输入数据类型, 即编码解码的输入端
     *
     * @return 数据类型
     */
    public Class getSrcClass() {
        return srcClass;
    }

    /**
     * 获取输出数据类型, 即编码解码的输出端
     *
     * @return 数据类型
     */
    public Class getTgtClass() {
        return tgtClass;
    }

}
