package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.IDPool;
import com.github.sisyphsu.nakedata.context.ContextName;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 输出池
 *
 * @author sulin
 * @since 2019-05-03 16:03:10
 */
@Deprecated
public class OutputPool<T> {

    private static final long INIT_TIME = System.currentTimeMillis();
    private static final double FACTOR_KEEP = 0.9;

    /**
     * 池子容量
     */
    private int limit;
    /**
     * ID池, 封装递增ID分配会回收逻辑
     */
    private IDPool idPool;
    /**
     * 对象映射表
     */
    private Map<T, Active> map = new HashMap<>();

    /**
     * 初始化对象池
     *
     * @param limit 池子容量
     */
    public OutputPool(int limit) {
        this.limit = limit;
        this.idPool = new IDPool(limit);
        this.map = new HashMap<>();
    }

    public T build(T t) {
        Active ref = map.get(t);
        if (ref == null) {

            ref = new Active(t);
        }
        return null;
    }

    public class Active {

        /**
         * 激活次数
         */
        private short count;
        /**
         * 激活时间
         */
        private int time;

        private T data;

        public Active(T data) {
            this.data = data;
        }

        /**
         * 激活属性名
         */
        public void active() {
            this.time = (int) (System.currentTimeMillis() - INIT_TIME / 1000);
            this.count++;
        }

    }

}
