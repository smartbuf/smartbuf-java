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
     * Real name stored in global.
     */
    private String name;
    /**
     * The unique id of name in context.
     */
    private int id;
    /**
     * Reference count, used for ranking.
     */
    private int rcount;
    /**
     * score
     */
    private int score;
    /**
     * Relative time, used for ranking.
     */
    private final int rtime = (int) (System.currentTimeMillis() - INIT_TIME / 1000);

}
