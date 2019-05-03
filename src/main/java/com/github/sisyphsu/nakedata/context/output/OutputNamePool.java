package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.IDPool;
import com.github.sisyphsu.nakedata.context.ContextName;

import java.util.HashMap;
import java.util.Map;

/**
 * 输出上下文的变量名池
 *
 * @author sulin
 * @since 2019-04-29 13:39:46
 */
public class OutputNamePool {

    private static final double FACTOR_KEEP = 0.9;

    /**
     * When reach limit, keep some more active names.
     */
    private int keep;
    /**
     * Max count of name cached in Context.
     */
    private int limit;
    /**
     * Provide incremental id acquire and release features.
     */
    private IDPool pool;
    /**
     * Maintain the relationship between name and ContextName.
     */
    private Map<String, ActiveRecord<ContextName>> nameMap = new HashMap<>();

    /**
     * Initialize pool
     *
     * @param limit Name's count limit.
     */
    public OutputNamePool(int limit) {
        this.limit = limit;
        this.keep = (int) (limit * FACTOR_KEEP);
        this.pool = new IDPool(limit);
    }

    /**
     * Get the unique id of the specified name.
     *
     * @param name name's value
     * @return unique id
     */
    public int getNameID(String name) {
        ActiveRecord<ContextName> cxtName = nameMap.get(name);
        return cxtName.getData().getId();
    }

    /**
     * 向名称池中增加元素, 如果重复操作则激活它
     *
     * @param name 属性名
     * @return 处理结果
     */
    public ContextName addName(String name) {
        ActiveRecord<ContextName> ref = nameMap.get(name);
        if (ref == null) {
            int id = pool.acquire();
            ref = new ActiveRecord<>(new ContextName(id, name));
            nameMap.put(name, ref);
            // TODO 增加clog
        }
        ref.active(); // 激活一次

        return ref.getData();
    }

    /**
     * 尝试释放一些失去活性的属性名, 避免其无限制膨胀
     */
    public void tryRelease() {
        if (nameMap.size() < limit) {
            return;
        }
//        ActiveHeap<ContextName> heap = new ActiveHeap<>(limit - keep);
//        for (ActiveRef<ContextName> value : nameMap.values()) {
//            heap.filter(value);
//        }
//        heap.forEach(cxtName -> {
//            nameMap.remove(cxtName.getName());
//            pool.release(cxtName.getId());
//            // TODO 增加clog
//        });
    }

    public static class OutputName extends ContextName {

        private ActiveRecord record;

        public OutputName(int id, String name) {
            super(id, name);
        }

    }

}
