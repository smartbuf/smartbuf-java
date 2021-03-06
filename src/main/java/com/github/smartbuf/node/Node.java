package com.github.smartbuf.node;

/**
 * Base class of Node, and provider some common features.
 *
 * @author sulin
 * @since 2019-05-08 20:33:51
 */
public abstract class Node {

    /**
     * Get this Node's value
     *
     * @return Node's real value
     */
    public abstract Object value();

    /**
     * Get this Node's datatype
     *
     * @return Node's real type
     */
    public abstract NodeType type();

}
