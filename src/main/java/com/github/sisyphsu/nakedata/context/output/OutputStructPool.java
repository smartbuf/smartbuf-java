package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.model.ContextName;
import com.github.sisyphsu.nakedata.context.model.ContextStruct;
import com.github.sisyphsu.nakedata.context.model.ContextVersion;
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
    private Map<ContextStruct, OutputStruct> map;

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
    public ContextStruct buildStruct(ContextVersion log, ContextName[] names) {
        OutputStruct temp = new OutputStruct(names);
        OutputStruct result = map.get(temp);
        if (result == null) {
            temp.setId(pool.acquire());
            map.put(temp, temp);
            log.getStructAdded().add(temp); // 记录struct-added
            result = temp;
        }
        result.active();

        return result;
    }

    /**
     * 尝试释放一些
     */
    public void tryRelease(ContextVersion log) {
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
        // 废弃不活跃, 记录structExpired
        heap.forEach(struct -> {
            map.remove(struct);
            pool.release(struct.getId());
            log.getStructExpired().add(struct.getId());
        });
        this.releaseTime = time();
    }

    /**
     * 输出端拓展的ContextStruct, 额外增加了活跃度监控功能
     */
    @Getter
    public class OutputStruct extends ContextStruct implements GCHeap.Score {

        private int count;
        private int time;

        public OutputStruct(ContextName[] names) {
            super(names);
        }

        public void active() {
            this.time = time();
            this.count++;
        }

    }

}
