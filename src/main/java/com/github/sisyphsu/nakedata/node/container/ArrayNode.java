package com.github.sisyphsu.nakedata.node.container;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.type.DataType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sulin
 * @since 2019-05-08 21:02:18
 */
@Getter
@Setter
public class ArrayNode extends Node {

    public final static ArrayNode NULL = new ArrayNode(null);

    private final List<Node> items;

    /**
     * 数组内部按类型分组
     */
    private transient List<Group> groups = new ArrayList<>();

    private ArrayNode(List<Node> items) {
        this.items = items;
    }

    public static ArrayNode valueOf(List<Node> items) {
        if (items == null)
            return NULL;
        return new ArrayNode(items);
    }

    @Override
    public DataType getDataType() {
        return DataType.ARRAY;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

    @Data
    public static class Group {
        private byte typeCode;
        private ContextType type;
        private int count;
        private boolean end;
    }

}
