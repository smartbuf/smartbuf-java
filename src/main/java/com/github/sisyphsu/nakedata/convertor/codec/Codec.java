package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 数据转换适配器
 * <p>
 * encode函数约定为[to*], 负责将T类型序列化为目标数据, 输入参数仅接收T实例
 * <p>
 * decode函数约定为[from*], 负责将指定类型数据反序列化为T实例, 部分类型需要提供泛型类型
 * 1. POJO, 需要外界指定真实的POJO类型
 * 2. Array, 需要外界指定数组内部Type
 * 3. Collection, 需要外界提供集合泛型Type
 * 4. Map, 需要外界提供泛型Type
 *
 * @author sulin
 * @since 2019-05-12 16:01:19
 */
public abstract class Codec {

    private CodecFactory factory;

    public void setFactory(CodecFactory factory) {
        this.factory = factory;
    }

    /**
     * Factory's doConvert convience.
     *
     * @param src     Source Data
     * @param tgtType Target Type
     * @return Target Instance
     */
    public Object convert(Object src, Type tgtType) {
        return factory.doConvert(src, tgtType);
    }

    /**
     * Factory's doConvert convinence.
     *
     * @param src source data
     * @param clz target class
     * @param <Z> Target template type
     * @return target instance
     */
    @SuppressWarnings("unchecked")
    public <Z> Z convert(Object src, Class<Z> clz) {
        return (Z) factory.doConvert(src, clz);
    }

    /**
     * Get Array Class's ComponentType, throw Exception directly if not
     *
     * @param type Type
     * @return Array Class's ComponentType
     */
    public static Class<?> forceParseArrayComponentType(Type type) {
        if (!(type instanceof Class)) {
            throw new IllegalArgumentException("need Class: " + type);
        }
        Class clz = (Class) type;
        if (!clz.isArray()) {
            throw new IllegalArgumentException("need Array Class: " + type);
        }
        return clz.getComponentType();
    }

    /**
     * Get Parameterized Class's GenericType, like Long for List<Long>
     *
     * @param type Type
     * @return Parameterized Class's ActualType
     */
    public static Type getGenericType(Type type) {
        if (!(type instanceof ParameterizedType)) {
            return null;
        }
        ParameterizedType pt = (ParameterizedType) type;
        Type[] types = pt.getActualTypeArguments();
        if (types.length == 0) {
            return null;
        }
        return types[0];
    }

}
