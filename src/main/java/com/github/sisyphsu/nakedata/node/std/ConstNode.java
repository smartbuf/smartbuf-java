package com.github.sisyphsu.nakedata.node.std;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * @author sulin
 * @since 2019-06-04 20:23:31
 */
public class ConstNode extends Node {

    @Override
    public DataType getDataType() {
        return null;
    }

    @Override
    public boolean isNull() {
        return false;
    }

}
