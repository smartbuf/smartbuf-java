package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;
import sun.reflect.MethodAccessor;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * 编码解码过程中的单个类型转换步骤
 * <p>
 * codec初始化之后，扫描其支持的from/to规则，根据方法的From和To类型构建Step实例
 *
 * @author sulin
 * @since 2019-05-12 15:41:15
 */
public class DecodeStep {

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
    public DecodeStep(Codec codec, Method method) {
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
     * @param type 操作相关的数据类型
     * @param src  原始数据
     * @return 目标类型
     */
    public Object convert(Type type, Object src) {
        try {
            return accessor.invoke(codec, new Object[]{type, src});
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
