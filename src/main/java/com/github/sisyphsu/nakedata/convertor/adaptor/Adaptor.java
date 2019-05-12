package com.github.sisyphsu.nakedata.convertor.adaptor;

import java.lang.reflect.ParameterizedType;

/**
 * 数据转换适配器
 *
 * @author sulin
 * @since 2019-05-12 16:01:19
 */
@SuppressWarnings("unchecked")
public abstract class Adaptor<Src, Tgt> {

    /**
     * 获取Src类型
     *
     * @return src类型
     */
    public Class<Src> getSrcType() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<Src>) type.getActualTypeArguments()[0];
    }

    /**
     * 获取Tgt类型
     *
     * @return tgt类型
     */
    public Class<Tgt> getTgtType() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        return (Class<Tgt>) type.getActualTypeArguments()[1];
    }

    /**
     * 将指定src对象转换为tgt对象
     *
     * @param src src对象
     * @return tgt实例
     */
    public Tgt toTarget(Src src) {
        return src == null ? null : this.toTargetNotNull(src);
    }

    /**
     * 将指定tgt对象转换为src实例
     *
     * @param tgt tgt对象
     * @return src实例
     */
    public Src toSource(Tgt tgt) {
        return tgt == null ? null : this.toSourceNotNull(tgt);
    }

    /**
     * 非空src转换为Tgt接口, 由子类实现
     *
     * @param src 非空的src对象
     * @return Tgt实例
     */
    protected abstract Tgt toTargetNotNull(Src src);

    /**
     * 非空tgt转换为src接口, 由子类实现
     *
     * @param tgt 非空的Tgt对象
     * @return src实例
     */
    protected abstract Src toSourceNotNull(Tgt tgt);

}
