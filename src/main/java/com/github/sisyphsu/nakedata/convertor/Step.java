package com.github.sisyphsu.nakedata.convertor;

/**
 * 类型转换标准定义
 *
 * @author sulin
 * @since 2019-05-12 15:41:15
 */
@FunctionalInterface
public interface Step<From, To> {

    /**
     * 数据转换函数
     *
     * @param f 原始数据
     * @return 转换目标
     */
    To convert(From f);

}
