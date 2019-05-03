package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.IDPool;
import com.github.sisyphsu.nakedata.context.ContextStruct;
import com.github.sisyphsu.nakedata.context.ContextType;

import java.util.HashMap;
import java.util.Map;

/**
 * TypePool for output side.
 *
 * @author sulin
 * @since 2019-04-30 18:06:44
 */
public class OutputTypePool {

    /**
     * 类型池最大容量, 超过后需要执行垃圾回收
     */
    private final int max;
    /**
     * ID池, 用于分配递增的ID
     */
    private IDPool pool;
    /**
     * 类型表, 当前已分配的全部数据类型
     */
    private Map<ContextType, ActiveRecord<ContextType>> typeMap;

    public OutputTypePool(int max) {
        this.max = max;
        this.pool = new IDPool(max);
        this.typeMap = new HashMap<>();
    }

    /**
     * 根据struct和types构建上下文数据类型, 如果已存在则直接用旧数据
     *
     * @param struct 数据结构
     * @param types  数据结构对应的成员类型
     * @return 自定义数据类型
     */
    public ContextType buildType(ContextStruct struct, int[] types) {
        ContextType type = new ContextType(types, struct);
        ActiveRecord<ContextType> result = typeMap.get(type);
        if (result == null) {
            type = new ContextType(pool.acquire());
            type.setTypes(types);
            type.setStruct(struct);
            result = new ActiveRecord<>(type);
        }
        result.active();

        return result.getData();
    }

    /**
     * 尝试执行数据类型回收, 如果池满了的话
     */
    public void release() {

    }

}
