package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.model.ContextStruct;
import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.context.model.ContextVersion;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * TypePool for output side.
 *
 * @author sulin
 * @since 2019-04-30 18:06:44
 */
public class OutputTypePool extends BasePool {

    /**
     * 类型表, 当前已分配的全部数据类型
     */
    private Map<OutputType, OutputType> typeMap;

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
    public ContextType buildType(ContextVersion version, ContextStruct struct, int[] types) {
        OutputType temp = new OutputType(types, struct);
        OutputType result = typeMap.get(temp);
        if (result == null) {
            temp.setId(pool.acquire());
            result = temp;
            typeMap.put(result, result);
            version.getTypeAdded().add(result); // 记录type-added
        }
        result.active();
        return result;
    }

    /**
     * 尝试执行数据类型回收, 如果池满了的话
     */
    public void tryRelease(ContextVersion log) {
        if (typeMap.size() < limit) {
            return;
        }
        GCHeap<OutputType> heap = new GCHeap<>(limit / 10);
        for (OutputType type : typeMap.values()) {
            if (type.time >= this.releaseTime) {
                continue;
            }
            heap.filter(type);
        }
        // 废弃不活跃, 记录typeExpired
        heap.forEach(type -> {
            typeMap.remove(type);
            pool.release(type.getId());
            log.getTypeExpired().add(type.getId());
        });
        this.releaseTime = time();
    }

    @Getter
    public class OutputType extends ContextType implements GCHeap.Score {

        private int count;
        private int time;

        public OutputType(int[] types, ContextStruct struct) {
            super(types, struct);
        }

        public void active() {
            this.time = time();
            this.count++;
        }

    }

}
