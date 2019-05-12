package com.github.sisyphsu.nakedata.codec;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.NodeMapper;

/**
 * @author sulin
 * @since 2019-05-12 12:14:05
 */
public abstract class Codec<T> {

    protected final Class<T> type;
    protected final NodeMapper mapper;

    public Codec(Class<T> type, NodeMapper mapper) {
        this.type = type;
        this.mapper = mapper;
    }

    /**
     * 将Java实例转换为Node
     *
     * @param obj 泛型对象
     * @return Node节点
     */
    public abstract Node encode(T obj);

    /**
     * 将标准Node节点转换为Java实例
     *
     * @param node Node节点
     * @return 泛型实例
     */
    public abstract T decode(Node node);

}
