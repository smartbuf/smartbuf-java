package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.type.DataType;

/**
 * 属性树节点
 *
 * @author sulin
 * @since 2019-05-08 20:33:51
 */
public abstract class AbstractNode {

    // 序列化
    public void serialize() {
    }

    // 反序列化
    public void deserialize() {
    }

    /**
     * 节点的数据类型
     *
     * @return 类型枚举
     */
    public abstract DataType getType();

    /**
     * 是否是null值
     *
     * @return true表示null值
     */
    public abstract boolean isNull();

}
