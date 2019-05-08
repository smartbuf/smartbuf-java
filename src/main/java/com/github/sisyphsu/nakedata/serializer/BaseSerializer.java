package com.github.sisyphsu.nakedata.serializer;

import com.github.sisyphsu.nakedata.DataType;

/**
 * @author sulin
 * @since 2019-05-08 20:43:53
 */
public abstract class BaseSerializer<T> {

    // 支持的Java类型
    public abstract Class<T> javaType();

    // 序列化后的标准类型
    public abstract DataType type();

}
