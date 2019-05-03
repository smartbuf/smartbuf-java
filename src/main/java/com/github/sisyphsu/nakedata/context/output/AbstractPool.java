package com.github.sisyphsu.nakedata.context.output;

import com.github.sisyphsu.nakedata.common.IDPool;

/**
 * @author sulin
 * @since 2019-05-03 16:51:32
 */
public class AbstractPool {

    private static final long INIT_TIME = System.currentTimeMillis();

    /**
     * 类型池最大容量, 超过后需要执行垃圾回收
     */
    protected final int limit;
    /**
     * ID池, 用于分配递增的ID
     */
    protected IDPool pool;
    /**
     * 执行release操作的时间戳
     */
    protected int releaseTime;

    /**
     * 构造基础池
     *
     * @param limit 最大容量
     */
    public AbstractPool(int limit) {
        this.limit = limit;
        this.pool = new IDPool(Integer.MAX_VALUE);
    }

    /**
     * 获取当前进程已启动的秒数
     *
     * @return 秒
     */
    public int time() {
        return (int) (System.currentTimeMillis() - INIT_TIME / 1000);
    }

}
