package com.github.sisyphsu.nakedata.context;

import lombok.Data;

/**
 * @author sulin
 * @since 2019-04-29 13:17:31
 */
@Data
public class ContextName {

    private static final long INIT_TIME = System.currentTimeMillis();

    /**
     * The unique id of name in context.
     */
    private final int id;
    /**
     * Real name stored in global.
     */
    private final String name;

    /**
     * Reference count, used for ranking.
     */
    private short rcount;
    /**
     * Relative time, used for ranking.
     */
    private int rtime;

    /**
     * Initialize ContextName
     *
     * @param id   Unique id
     * @param name Real name
     */
    public ContextName(int id, String name) {
        this.id = id;
        this.name = name;
        this.flushRTime();
    }

    /**
     * flush relative time
     */
    public void flushRTime() {
        this.rtime = (int) (System.currentTimeMillis() - INIT_TIME / 1000);
    }

}
