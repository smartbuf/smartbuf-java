package com.github.sisyphsu.nakedata.convertor;

/**
 * @author sulin
 * @since 2019-05-21 21:01:27
 */
public interface CodecMethod {

    /**
     * 获取输入数据类型, 即编码解码的输入端
     *
     * @return 数据类型
     */
    Class getSrcClass();

    /**
     * 获取输出数据类型, 即编码解码的输出端
     *
     * @return 数据类型
     */
    Class getTgtClass();

}
