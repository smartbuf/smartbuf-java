package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.type.DataType;

/**
 * 属性树节点
 *
 * @author sulin
 * @since 2019-05-08 20:33:51
 */
public abstract class Node {

    // 序列化
    public void serialize() {
    }

    // 反序列化
    public void deserialize() {
    }

    /**
     * 是否是null值
     *
     * @return true表示null值
     */
    @Deprecated
    public abstract boolean isNull();

    /**
     * Get Node's dataType
     *
     * @return DataType
     */
    public abstract DataType dataType();

}
