package com.github.sisyphsu.nakedata.convertor;

import java.lang.reflect.Type;

/**
 * 编排Codec的数据转换处理器
 *
 * @author sulin
 * @since 2019-05-19 17:23:23
 */
public class EncodePipeline {

    private Class srcType;
    private Class tgtType;
    private DecodeStep[] steps;

    /**
     * 执行处理器，将指定src数据转换为预定类型
     *
     * @param type src数据类型
     * @param src  原始数据, 可以是null
     * @return 处理结果, 可能是null
     */
    public Object convert(Type type, Object src) {
        // TODO type传递挺麻烦的啊
        for (DecodeStep step : steps) {
//            step.convert(type, src);
        }
        return null;
    }

}
