package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.context.model.ContextName;
import com.github.sisyphsu.nakedata.context.model.ContextVersion;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 输出上下文的变量名池
 *
 * @author sulin
 * @since 2019-04-29 13:39:46
 */
public class OutputNamePool extends BasePool {

    private Map<String, OutputName> cxtNameMap = new HashMap<>();
    private Map<String, ContextName> tmpNameMap = new HashMap<>();

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
     * @param version 上下文版本
     * @param name    变量名
     * @return ContextName
     */
    public ContextName buildCxtName(ContextVersion version, String name) {
        OutputName result = cxtNameMap.get(name);
        if (result == null) {
            int id = pool.acquire();
            result = new OutputName(id, name);
            cxtNameMap.put(name, result);
            // 记录元数据变化
            version.getNameAdded().add(result);
        }
        result.active(); // 激活一次

        return result;
    }

    /**
     * 构建临时的上下文变量名
     *
     * @param version 上下文版本
     * @param name    变量名
     * @return ContextName
     */
    public ContextName buildTmpName(ContextVersion version, String name) {
        ContextName result = tmpNameMap.get(name);
        if (result == null) {
            int id = (tmpNameMap.size() + 1) * -1;
            result = new ContextName(id, name);
            tmpNameMap.put(name, result);

            version.getNameTemp().add(result);
        }
        return result;
    }

    /**
     * 尝试释放一些失去活性的属性名, 避免其无限制膨胀
     */
    public void release(ContextVersion version) {
        this.tmpNameMap.clear();

        if (cxtNameMap.size() < limit) {
            return;
        }
        GCHeap<OutputName> heap = new GCHeap<>(limit / 10);
        for (OutputName name : cxtNameMap.values()) {
            if (name.time >= this.releaseTime) {
                continue;
            }
            heap.filter(name);
        }
        // 废弃不活跃的名称, 记录nameExpired
        heap.forEach(name -> {
            cxtNameMap.remove(name.getName());
            pool.release(name.getId());
            version.getNameExpired().add(name.getId());
        });
        // 更新释放时间
        this.releaseTime = time();
    }

    /**
     * 输出端适配的ContextName对象, 拓展活跃度监控等
     */
    @Getter
    public class OutputName extends ContextName implements GCHeap.Score {

        private int count;
        private int time;

        public void active() {
            this.time = time();
            this.count++;
        }

        public OutputName(int id, String name) {
            super(id, name);
        }

    }

}
