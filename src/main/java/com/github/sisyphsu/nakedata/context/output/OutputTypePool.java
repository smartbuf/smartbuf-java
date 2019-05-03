package com.github.sisyphsu.nakedata.context.output;

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
public class OutputTypePool extends AbstractPool {

    /**
     * 类型表, 当前已分配的全部数据类型
     */
    private Map<ContextType, OutputType> typeMap;

    public OutputTypePool(int limit) {
        super(limit);
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
        OutputType result = typeMap.get(type);
        if (result == null) {
            int id = pool.acquire();
            type.setTypes(types);
            type.setStruct(struct);
            result = new OutputType(id, types, struct);
        }
        result.active();

        return result;
    }

    /**
     * 尝试执行数据类型回收, 如果池满了的话
     */
    public void tryRelease() {
        if (typeMap.size() < limit) {
            return;
        }
        ActiveHeap<OutputType> heap = new ActiveHeap<>(limit / 10);
        for (OutputType type : typeMap.values()) {
            if (type.time >= this.releaseTime) {
                continue;
            }
            heap.filter(type);
        }
        // TODO 废弃不活跃, 记录structExpired
        heap.forEach(type -> {
            typeMap.remove(null); // TypeKey
            pool.release(type.getId());
        });
        this.releaseTime = time();
    }

    public class OutputType extends ContextType implements ActiveHeap.Score {

        private short count;
        private int time;

        public OutputType(int id, int[] types, ContextStruct struct) {
            super(types, struct);
            super.setId(id);
        }

        public void active() {
            this.time = time();
            this.count++;
        }


        @Override
        public double getScore() {
            return this.count + this.time / 86400.0;
        }

    }

}
