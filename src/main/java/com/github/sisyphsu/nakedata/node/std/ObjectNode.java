package com.github.sisyphsu.nakedata.node.std;

import com.github.sisyphsu.nakedata.context.model.ContextType;
import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.type.DataType;
import lombok.Getter;
import lombok.Setter;

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

    public ObjectNode() {
        this(new TreeMap<>());
    }

    public ObjectNode(TreeMap<String, Node> fields) {
        this.fields = fields;
    }

    @Override
    public DataType dataType() {
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
