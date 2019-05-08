package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.type.DataType;

/**
 * 属性树节点
 *
 * @author sulin
 * @since 2019-05-08 20:33:51
 */
public abstract class AbstractNode {

    // 标准类型
    private transient DataType type;

    // 序列化
    public abstract void serialize();

    // 反序列化
    public abstract void deserialize();



}
