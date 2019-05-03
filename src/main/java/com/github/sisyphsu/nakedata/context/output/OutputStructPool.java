package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.IDPool;
import com.github.sisyphsu.nakedata.context.ContextName;
import com.github.sisyphsu.nakedata.context.ContextStruct;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 输出端的结构池
 *
 * @author sulin
 * @since 2019-05-02 20:32:10
 */
public class OutputStructPool {

    private final int max;
    /**
     * ID池
     */
    private IDPool pool;
    /**
     * 池子
     */
    private Map<ContextStruct, ActiveRef<ContextStruct>> poolMap;

    /**
     * 初始化结构池
     *
     * @param max 触发回收的最大容量
     */
    public OutputStructPool(int max) {
        this.max = max;
        this.pool = new IDPool(max);
        this.poolMap = new HashMap<>();
    }

    /**
     * 创建数据结构
     *
     * @param names 变量名列表
     * @return 数据结构实例
     */
    public ContextStruct buildStruct(Collection<ContextName> names) {
        ContextName[] arr = names.toArray(new ContextName[0]);
        ActiveRef<ContextStruct> ref = poolMap.get(new ContextStruct(arr));
        if (ref == null) {
            int id = pool.acquire();
            ContextStruct struct = new ContextStruct(id, arr);
            ref = new ActiveRef<>(struct);
            poolMap.put(struct, ref);
        }
        ref.active();

        return ref.getData();
    }

    /**
     * 尝试释放一些
     */
    public void release() {

    }

}
