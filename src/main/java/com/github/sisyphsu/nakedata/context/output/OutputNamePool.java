package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.ContextLog;
import com.github.sisyphsu.nakedata.context.ContextName;

import java.util.HashMap;
import java.util.Map;

/**
 * 输出上下文的变量名池
 *
 * @author sulin
 * @since 2019-04-29 13:39:46
 */
public class OutputNamePool extends BasePool {

    /**
     * Maintain the relationship between name and ContextName.
     */
    private Map<String, OutputName> nameMap = new HashMap<>();

    /**
     * Initialize pool
     *
     * @param limit Name's count limit.
     */
    public OutputNamePool(int limit) {
        super(limit);
    }

    /**
     * 根据名称字符串构建ContextName实例, 如果已有则直接获取旧实例。
     *
     * @param name name's value
     * @return unique id
     */
    public ContextName buildName(ContextLog log, String name) {
        OutputName cxtName = nameMap.get(name);
        if (cxtName == null) {
            int id = pool.acquire();
            cxtName = new OutputName(id, name);
            nameMap.put(name, cxtName);
            // 记录元数据变化
            log.getNameAdded().add(cxtName);
        }
        cxtName.active(); // 激活一次

        return cxtName;
    }

    /**
     * 尝试释放一些失去活性的属性名, 避免其无限制膨胀
     */
    public void tryRelease(ContextLog log) {
        if (nameMap.size() < limit) {
            return;
        }
        GCHeap<OutputName> heap = new GCHeap<>(limit / 10);
        for (OutputName name : nameMap.values()) {
            if (name.time >= this.releaseTime) {
                continue;
            }
            heap.filter(name);
        }
        // TODO 废弃不活跃active, 记录nameExpired
        heap.forEach(name -> {
            nameMap.remove(name.getName());
            pool.release(name.getId());
            log.getNameExpired().add(name.getId());
        });
        // 更新释放时间
        this.releaseTime = time();
    }

    /**
     * 输出端适配的ContextName对象, 拓展活跃度监控等
     */
    public class OutputName extends ContextName implements GCHeap.Score {

        private short count;
        private int time;

        public void active() {
            this.time = time();
            this.count++;
        }

        public OutputName(int id, String name) {
            super(id, name);
        }

        @Override
        public double getScore() {
            return this.count + this.time / 86400.0;
        }

    }

}
