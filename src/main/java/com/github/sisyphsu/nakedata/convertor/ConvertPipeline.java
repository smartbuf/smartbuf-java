package com.github.sisyphsu.nakedata.convertor;

import java.lang.reflect.Type;
import java.util.List;

/**
 * 编码编排Codec的数据Decode转换处理器, 如:
 * 1. string -> map -> POJO
 * 2. ObjectNode -> map -> POJO
 * 3. ByteNode -> byte[] -> Date
 *
 * @author sulin
 * @since 2019-05-20 15:24:12
 */
public class ConvertPipeline {

    private final Type srcType;
    private final Type tgtType;
    private final List<ConvertMethod> methods;

    private ConvertPipeline(Type srcType, Type tgtType, List<ConvertMethod> methods) {
        this.srcType = srcType;
        this.tgtType = tgtType;
        this.methods = methods;
    }

    public static ConvertPipeline valueOf(Type src, Type tgt) {
        return new ConvertPipeline(src, tgt, null);
    }

    public static ConvertPipeline valueOf(List<ConvertMethod> methods) {
        if (methods == null || methods.size() == 0) {
            throw new IllegalArgumentException("methods can't be null or empty");
        }
        Type srcType = methods.get(0).getSrcType();
        Type tgtType = methods.get(methods.size() - 1).getTgtType();
        return new ConvertPipeline(srcType, tgtType, methods);
    }

    /**
     * 将指定数据解码(反序列化)为指定类型实例
     *
     * @param data    待解码的数据
     * @param tgtType 目标类型, 如POJO类、携带泛型类型的集合类等
     * @return 解码结果
     */
    public Object convert(Object data, Type tgtType) {
        Object result = data;
        for (ConvertMethod method : methods) {
            result = method.convert(result, tgtType);
        }
        return result;
    }

    public Type getSrcType() {
        return srcType;
    }

    public Type getTgtType() {
        return tgtType;
    }

}