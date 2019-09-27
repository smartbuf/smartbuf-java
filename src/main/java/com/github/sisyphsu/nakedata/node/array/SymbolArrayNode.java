package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.ArrayType;

import java.util.List;

/**
 * @author sulin
 * @since 2019-09-27 20:14:39
 */
public class SymbolArrayNode extends ArrayNode {

    public SymbolArrayNode(List items) {
        super(items);
    }

    @Override
    public ArrayType elementType() {
        return ArrayType.SYMBOL;
    }
    
}
