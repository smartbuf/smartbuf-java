package com.github.sisyphsu.nakedata.convertor.pipeline;

import java.lang.reflect.Type;

/**
 * 编码编排Codec的数据Decode转换处理器, 如:
 * 1. string -> map -> POJO
 * 2. ObjectNode -> map -> POJO
 * 3. ByteNode -> byte[] -> Date
 *
 * @author sulin
 * @since 2019-05-20 15:24:12
 */
public class DecodePipeline {

    private final Class srcClass;
    private final Class tgtClass;
    private final DecodeMethod[] chain;

    /**
     * 初始化Decode流水线
     *
     * @param methods 解码链路
     */
    public DecodePipeline(DecodeMethod... methods) {
        if (methods == null || methods.length == 0) {
            throw new IllegalArgumentException("decode methods can't be empty");
        }
        this.chain = methods;
        this.srcClass = methods[0].getSrcClass();
        this.tgtClass = methods[methods.length - 1].getTgtClass();
    }

    /**
     * 将指定数据解码(反序列化)为指定类型实例
     *
     * @param type 目标类型, 如POJO类、携带泛型类型的集合类等
     * @param data 待解码的数据
     * @return 解码结果
     */
    public Object decode(Type type, Object data) {
        Object result = data;
        for (DecodeMethod method : chain) {
            result = method.decode(type, result);
        }
        return result;
    }

    public Class getSrcClass() {
        return srcClass;
    }

    public Class getTgtClass() {
        return tgtClass;
    }
}
