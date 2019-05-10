package com.github.sisyphsu.nakedata.node.container;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.type.DataType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author sulin
 * @since 2019-05-08 21:02:12
 */
@Getter
@Setter
public class ObjectNode extends Node {

    public final static ObjectNode NULL = new ObjectNode(null);

    private final TreeMap<String, Node> fields;

    /**
     * Object节点的元数据信息
     */
    private ContextType contextType;

    private ObjectNode(TreeMap<String, Node> fields) {
        this.fields = fields;
    }

    public static ObjectNode newInstance(Map<String, Node> fields) {
        if (fields == null) {
            return NULL;
        }
        if (fields instanceof TreeMap) {
            return new ObjectNode((TreeMap<String, Node>) fields);
        }
        return new ObjectNode(new TreeMap<>(fields));
    }

    @Override
    public DataType getDataType() {
        return DataType.OBJECT;
    }

    @Override
    public boolean isNull() {
        return this == NULL;
    }

    public int size() {
        return fields.size();
    }

}
