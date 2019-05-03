package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.ContextLog;
import com.github.sisyphsu.nakedata.context.ContextName;
import com.github.sisyphsu.nakedata.context.ContextStruct;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 输出端的结构池
 *
 * @author sulin
 * @since 2019-05-02 20:32:10
 */
public class OutputStructPool extends BasePool {

    /**
     * 池子
     */
    private Map<StructKey, OutputStruct> map;

    /**
     * 初始化结构池
     *
     * @param limit 触发回收的最大容量
     */
    public OutputStructPool(int limit) {
        super(limit);
        this.map = new HashMap<>();
    }

    /**
     * 创建数据结构
     *
     * @param names 变量名列表
     * @return 数据结构实例
     */
    public ContextStruct buildStruct(ContextLog log, ContextName[] names) {
        StructKey key = new StructKey(names);
        OutputStruct struct = map.get(key);
        if (struct == null) {
            int id = pool.acquire();
            struct = new OutputStruct(id, key.getNames());
            map.put(key, struct);
            // 记录struct-added
            log.getStructAdded().add(struct);
        }

        struct.active();

        return struct;
    }

    /**
     * 尝试释放一些
     */
    public void tryRelease(ContextLog log) {
        if (map.size() < limit) {
            return;
        }
        GCHeap<OutputStruct> heap = new GCHeap<>(limit / 10);
        for (OutputStruct struct : map.values()) {
            if (struct.time >= this.releaseTime) {
                continue;
            }
            heap.filter(struct);
        }
        // TODO 废弃不活跃, 记录structExpired
        heap.forEach(struct -> {
            map.remove(new StructKey(struct.getNames())); // IT'S OK
            pool.release(struct.getId());
            log.getStructExpired().add(struct.getId());
        });
        this.releaseTime = time();
    }

    /**
     * 类型Key
     */
    @Getter
    public static class StructKey {

        private final ContextName[] names;

        private StructKey(ContextName[] names) {
            this.names = names;
        }

        @Override
        public int hashCode() {
            int result = 1;
            for (ContextName name : names) {
                result = 31 * result + name.getId();
            }
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof StructKey)) {
                return false;
            }
            StructKey other = (StructKey) obj;
            if (this.names.length != other.names.length) {
                return false;
            }
            for (int i = 0; i < names.length; i++) {
                ContextName left = names[i];
                ContextName right = other.names[i];
                if (left.getId() != right.getId()) {
                    return false;
                }
            }
            return true;
        }

    }

    /**
     * 输出端拓展的ContextStruct, 额外增加了活跃度监控功能
     */
    public class OutputStruct extends ContextStruct implements GCHeap.Score {

        private short count;
        private int time;

        public void active() {
            this.time = time();
            this.count++;
        }

        public OutputStruct(int id, ContextName[] names) {
            super(id, names);
        }

        @Override
        public double getScore() {
            return this.count + this.time / 86400.0;
        }

    }

}
