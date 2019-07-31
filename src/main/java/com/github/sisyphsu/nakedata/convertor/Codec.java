package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.reflect.XType;
import com.github.sisyphsu.nakedata.convertor.reflect.XTypeUtils;

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
    public final Object convert(Object src, XType tgtType) {
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
    public final <Z> Z convert(Object src, Class<Z> clz) {
        return (Z) factory.doConvert(src, XTypeUtils.toXType(clz));
    }

}
