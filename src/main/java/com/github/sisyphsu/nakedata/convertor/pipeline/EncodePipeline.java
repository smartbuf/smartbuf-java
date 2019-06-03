package com.github.sisyphsu.nakedata.convertor.pipeline;

/**
 * 编排Codec的数据转换处理器, 如以下规则:
 * 1. POJO -> map -> string
 * 2. POJO -> map -> ObjectNode
 * 3. Enum -> Const -> ConstNode
 * 4. Date -> byte[] -> BinaryNode
 *
 * @author sulin
 * @since 2019-05-19 17:23:23
 */
public class EncodePipeline {

    private Class srcType;
    private Class tgtType;
    private EncodeMethod[] chain;

    /**
     * 初始化, 根据指定的编码方法
     *
     * @param methods 数据转换链路
     */
    public EncodePipeline(EncodeMethod... methods) {
        if (methods == null || methods.length == 0) {
            throw new IllegalArgumentException("encode methods can't be empty");
        }
        this.chain = methods;
        this.srcType = methods[0].getSrcClass();
        this.tgtType = methods[methods.length - 1].getTgtClass();
    }

    /**
     * 执行处理器，将指定src数据转换为预定类型
     *
     * @param src 源数据, 可以是null
     * @return 编码结果, 可能是null
     */
    public Object convert(Object src) {
        Object result = src;
        for (EncodeMethod method : chain) {
            result = method.encode(result);
        }
        return result;
    }

    public Class getSrcType() {
        return srcType;
    }

    public Class getTgtType() {
        return tgtType;
    }

}
