package com.github.sisyphsu.nakedata.node.array;

import java.util.List;

/**
 * PureArrayNode
 * @author sulin
 * @since 2019-07-04 19:56:59
 */
public class PureArrayNode extends ArrayNode {

    private Object[] data;

    protected PureArrayNode(Object[] arr) {
        super(null);
        this.data = arr;
    }

}
