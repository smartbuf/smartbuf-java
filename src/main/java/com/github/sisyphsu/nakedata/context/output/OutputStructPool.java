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
    private Map<OutputStruct, OutputStruct> cxtStructMap = new HashMap<>();

    private Map<ContextStruct, ContextStruct> tmpStructMap = new HashMap<>();

    /**
     * 初始化结构池
     *
     * @param limit 触发回收的最大容量
     */
    public OutputStructPool(int limit) {
        super(limit);
    }

    /**
     * 创建数据结构
     *
     * @param version 上下文版本
     * @param names   结构成员
     * @return 数据结构实例
     */
    public ContextStruct buildCxtStruct(ContextVersion version, ContextName[] names) {
        OutputStruct temp = new OutputStruct(names);
        OutputStruct result = cxtStructMap.get(temp);
        if (result == null) {
            temp.setId(pool.acquire());
            cxtStructMap.put(temp, temp);
            result = temp;

            version.getStructAdded().add(temp); // 记录struct-added
        }
        result.active();

        return result;
    }

    /**
     * 创建临时的上下文数据结构
     *
     * @param version 上下文版本
     * @param names   结构成员
     * @return 数据结构实例
     */
    public ContextStruct buildTmpStruct(ContextVersion version, ContextName[] names) {
        ContextStruct temp = new ContextStruct(names);
        ContextStruct result = tmpStructMap.get(temp);
        if (result == null) {
            int id = -1 - tmpStructMap.size();
            result = temp;
            result.setId(id);
            tmpStructMap.put(result, result);

            version.getStructTemp().add(result);
        }
        return result;
    }

    /**
     * 尝试释放一些
     */
    public void release(ContextVersion log) {
        tmpStructMap.clear();
        
        if (cxtStructMap.size() < limit) {
            return;
        }
        GCHeap<OutputStruct> heap = new GCHeap<>(limit / 10);
        for (OutputStruct struct : cxtStructMap.values()) {
            if (struct.time >= this.releaseTime) {
                continue;
            }
            heap.filter(struct);
        }
        // 废弃不活跃, 记录structExpired
        heap.forEach(struct -> {
            cxtStructMap.remove(struct);
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
