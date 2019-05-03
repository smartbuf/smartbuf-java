package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.IDPool;
import com.github.sisyphsu.nakedata.context.ContextName;
import com.github.sisyphsu.nakedata.context.ContextVersion;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    private Map<String, ActiveRef<ContextName>> nameMap = new HashMap<>();

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
        ActiveRef<ContextName> cxtName = nameMap.get(name);
        return cxtName.getData().getId();
    }

    /**
     * 向名称池中增加元素, 如果重复操作则激活它
     *
     * @param name 属性名
     * @return 处理结果
     */
    public ContextName addName(String name) {
        ActiveRef<ContextName> ref = nameMap.get(name);
        if (ref == null) {
            int id = pool.acquire();
            ref = new ActiveRef<>(new ContextName(id, name));
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
        ActiveHeap<ContextName> heap = new ActiveHeap<>(limit - keep);
        for (ActiveRef<ContextName> value : nameMap.values()) {
            heap.filter(value);
        }
        heap.forEach(cxtName -> {
            nameMap.remove(cxtName.getName());
            pool.release(cxtName.getId());
            // TODO 增加clog
        });
    }

    /**
     * Add new names into the current pool, if name's count reach limit, auto-release some unactive ids.
     *
     * @param version TODO should use Context's reference, not argument.
     * @param names   Could be new names, or old names.
     */
    @Deprecated
    public void addNames(ContextVersion version, Set<String> names) {
        // if reach limit, expire some low priority name.
        if (names.size() + nameMap.size() > limit) {
            ActiveHeap<ContextName> heap = new ActiveHeap<>(limit - keep);
            for (ActiveRef<ContextName> name : nameMap.values()) {
                if (names.contains(name.getData().getName())) {
                    continue;
                }
                heap.filter(name);
            }
            heap.forEach(cxtName -> {
                nameMap.remove(cxtName.getName());
                pool.release(cxtName.getId());
                version.getNameExpired().add(cxtName.getId());
            });
        }
        // only add the name which didn't exists.
        for (String name : names) {
            ActiveRef<ContextName> cxtName = nameMap.get(name);
            if (cxtName == null) {
                cxtName = new ActiveRef<>(new ContextName(pool.acquire(), name));
                nameMap.put(cxtName.getData().getName(), cxtName);
                version.getNameAdded().add(name);
            }
            cxtName.active();
        }
    }

}
