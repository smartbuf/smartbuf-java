package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.type.DataType;

/**
 * symbol[] array, can't contains null.
 *
 * @author sulin
 * @since 2019-06-05 16:09:58
 */
public class SymbolArrayNode extends ArrayNode {

    public static final SymbolArrayNode NULL = new SymbolArrayNode(null);
    public static final SymbolArrayNode EMPTY = new SymbolArrayNode(new String[0]);

    private String[] items;

    private SymbolArrayNode(String[] items) {
        this.items = items;
    }

    public static SymbolArrayNode valueOf(String[] items) {
        if (items == null) {
            return NULL;
        }
        if (items.length == 0) {
            return EMPTY;
        }
        return new SymbolArrayNode(items);
    }

    @Override
    public int size() {
        return items.length;
    }

    @Override
    public DataType dataType() {
        return DataType.SYMBOL;
    }

    @Override
    public ContextType contextType() {
        return null;
    }

}
