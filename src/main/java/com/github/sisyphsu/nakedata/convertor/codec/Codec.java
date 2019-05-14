package com.github.sisyphsu.nakedata.convertor.codec;

import java.lang.reflect.ParameterizedType;

/**
 * 数据转换适配器
 *
 * @author sulin
 * @since 2019-05-12 16:01:19
 */
@SuppressWarnings("unchecked")
public abstract class Codec<T> {

    /**
     * 获取实现类支持的数据类型
     *
     * @return 泛型类型
     */
    public Class<T> getType() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<T>) type.getActualTypeArguments()[0];
    }

}
