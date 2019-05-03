package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.ContextLog;
import com.github.sisyphsu.nakedata.context.ContextStruct;
import com.github.sisyphsu.nakedata.context.ContextType;

import java.util.Arrays;
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
    private Map<TypeKey, OutputType> typeMap;

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
    public ContextType buildType(ContextLog log, ContextStruct struct, int[] types) {
        TypeKey key = new TypeKey(types, struct);
        OutputType result = typeMap.get(key);
        if (result == null) {
            int id = pool.acquire();
            result = new OutputType(id, types, struct);
            typeMap.put(key, result);
            // 记录type-added
            log.getTypeAdded().add(result);
        }
        result.active();
        return result;
    }

    /**
     * 尝试执行数据类型回收, 如果池满了的话
     */
    public void tryRelease(ContextLog log) {
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
        // TODO 废弃不活跃, 记录typeExpired
        heap.forEach(type -> {
            typeMap.remove(new TypeKey(type.getTypes(), type.getStruct())); // IT's OK
            pool.release(type.getId());
            log.getTypeExpired().add(type.getId());
        });
        this.releaseTime = time();
    }

    public class TypeKey {

        private final int[] types;
        private final ContextStruct struct;

        public TypeKey(int[] types, ContextStruct struct) {
            this.types = types;
            this.struct = struct;
        }

        @Override
        public int hashCode() {
            int result = 31 + struct.getId();
            for (int type : types) {
                result = 31 * result + type;
            }
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TypeKey) {
                TypeKey other = (TypeKey) obj;
                if (this.struct.getId() != other.struct.getId()) {
                    return false;
                }
                return Arrays.equals(this.types, other.types);
            }
            return false;
        }
    }

    public class OutputType extends ContextType implements GCHeap.Score {

        private short count;
        private int time;

        public OutputType(int id, int[] types, ContextStruct struct) {
            super(id, types, struct);
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
