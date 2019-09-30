package com.github.sisyphsu.nakedata.context.input;

import com.github.sisyphsu.nakedata.common.IDAllocator;

/**
 * Maintain type pool for input side.
 *
 * @author sulin
 * @since 2019-04-30 18:06:53
 */
public class InputStructPool {

    /**
     * The pool size of this TypePool, it mainly used for memory attack.
     */
    private final int         limit;
    /**
     * The IDPool which used for id allocation, it should work exactly same with output-side.
     */
    private       IDAllocator pool;

    /**
     * Initialize type pool for input side.
     *
     * @param limit The pool size.
     */
    public InputStructPool(int limit) {
        this.limit = limit;
        this.pool = new IDAllocator();
    }

}
