package com.github.sisyphsu.nakedata.convertor;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

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

    private final Class srcClass;
    private final Class tgtClass;
    private final List<ConvertMethod> methods;

    private ConvertPipeline(Class srcClass, Class tgtClass, List<ConvertMethod> methods) {
        this.srcClass = srcClass;
        this.tgtClass = tgtClass;
        this.methods = methods;
    }

    public static ConvertPipeline valueOf(Class src, Class tgt) {
        return new ConvertPipeline(src, tgt, null);
    }

    public static ConvertPipeline valueOf(List<ConvertMethod> methods) {
        if (methods == null || methods.size() == 0) {
            throw new IllegalArgumentException("methods can't be null or empty");
        }
        Class srcClass = methods.get(0).getSrcClass();
        Class tgtClass = methods.get(methods.size() - 1).getTgtClass();
        return new ConvertPipeline(srcClass, tgtClass, methods);
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

    public Class getSrcClass() {
        return srcClass;
    }

    public Class getTgtClass() {
        return tgtClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConvertPipeline that = (ConvertPipeline) o;

        return Objects.equals(srcClass, that.srcClass) && Objects.equals(tgtClass, that.tgtClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcClass, tgtClass);
    }

}