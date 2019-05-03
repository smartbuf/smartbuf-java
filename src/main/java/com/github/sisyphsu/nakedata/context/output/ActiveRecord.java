package com.github.sisyphsu.nakedata.context.output;

import lombok.Getter;

/**
 * 输出引用, 用于封装活跃度监控
 *
 * @author sulin
 * @since 2019-05-01 15:17:22
 */
@Getter
public class ActiveRecord<T> implements ActiveHeap.Score {

    private static final long INIT_TIME = System.currentTimeMillis();

    /**
     * Reference count, used for ranking.
     */
    private short rcount;
    /**
     * Relative time, used for ranking.
     */
    private int rtime;
    /**
     * 被封装的数据
     */
    private T data;

    public ActiveRecord(T data) {
        this.data = data;
    }

    /**
     * 激活属性名
     */
    public void active() {
        this.rtime = (int) (System.currentTimeMillis() - INIT_TIME / 1000);
        this.rcount++;
    }

    @Override
    public double getScore() {
        return this.rcount + this.rtime / 86400.0;
    }
}
