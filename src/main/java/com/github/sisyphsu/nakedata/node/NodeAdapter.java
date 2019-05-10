package com.github.sisyphsu.nakedata.node;

import lombok.Getter;

/**
 * 节点适配器, 负责Java类型与Node节点之间的数据转换
 *
 * @author sulin
 * @since 2019-05-09 21:20:56
 */
@Getter
public class NodeAdapter<T> {

    protected final Class<T> type;
    protected final NodeMapper mapper;

    public NodeAdapter(Class<T> type, NodeMapper mapper) {
        this.type = type;
        this.mapper = mapper;
    }

}
