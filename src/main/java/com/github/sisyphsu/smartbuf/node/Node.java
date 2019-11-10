package com.github.sisyphsu.smartbuf.node;

/**
 * Base class of Node, and provider some common features.
 *
 * @author sulin
 * @since 2019-05-08 20:33:51
 */
public abstract class Node {

    /**
     * Get this Node's value
     */
    public abstract Object value();

    /**
     * Get this Node's datatype
     */
    public abstract NodeType type();

}
