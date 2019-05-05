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
    private Map<OutputType, OutputType> cxtTypeMap = new HashMap<>();

    private Map<ContextType, ContextType> tmpTypeMap = new HashMap<>();

    public OutputTypePool(int limit) {
        super(limit);
    }

    /**
     * 根据struct和types构建上下文数据类型, 如果已存在则直接用旧数据
     *
     * @param struct 数据结构
     * @param types  数据结构对应的成员类型
     * @return 自定义数据类型
     */
    public ContextType buildCxtType(ContextVersion version, ContextStruct struct, int[] types) {
        OutputType temp = new OutputType(types, struct);
        OutputType result = cxtTypeMap.get(temp);
        if (result == null) {
            temp.setId(pool.acquire());
            result = temp;
            cxtTypeMap.put(result, result);
            version.getTypeAdded().add(result); // 记录type-added
        }
        result.active();
        return result;
    }

    /**
     * 根据数据结构即成员类型获取临时的自定义数据类型
     *
     * @param version 上下文版本
     * @param struct  数据结构
     * @param types   成员类型
     * @return 自定义数据类型
     */
    public ContextType buildTmpType(ContextVersion version, ContextStruct struct, int[] types) {
        ContextType temp = new ContextType(types, struct);
        ContextType result = tmpTypeMap.get(temp);
        if (result == null) {
            int id = -1 - tmpTypeMap.size();
            result = temp;
            result.setId(id);
            tmpTypeMap.put(result, result);

            version.getTypeTemp().add(result);
        }
        return result;
    }

    /**
     * 尝试执行数据类型回收, 如果池满了的话
     */
    public void tryRelease(ContextVersion version) {
        if (cxtTypeMap.size() < limit) {
            return;
        }
        GCHeap<OutputType> heap = new GCHeap<>(limit / 10);
        for (OutputType type : cxtTypeMap.values()) {
            if (type.time >= this.releaseTime) {
                continue;
            }
            heap.filter(type);
        }
        // 废弃不活跃, 记录typeExpired
        heap.forEach(type -> {
            cxtTypeMap.remove(type);
            pool.release(type.getId());
            version.getTypeExpired().add(type.getId());
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
