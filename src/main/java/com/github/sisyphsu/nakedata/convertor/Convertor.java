package com.github.sisyphsu.nakedata.convertor;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.List;

/**
 * 数据转换器, 封装各种数据类型之间的转换
 *
 * @author sulin
 * @since 2019-05-12 15:25:04
 */
public final class Convertor {

    private List<Codec> codecList;

    /**
     * 转换器构造方法, 需要指定所有可用的编码解码器
     *
     * @param codecs 全部可用编码解码器
     */
    public Convertor(List<Codec> codecs) {
        this.codecList = codecs;
    }

}
