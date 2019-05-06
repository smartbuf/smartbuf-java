package com.github.sisyphsu.nakedata.jackson.node;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.github.sisyphsu.nakedata.DataType;
import com.github.sisyphsu.nakedata.context.model.ContextType;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sulin
 * @since 2019-05-06 16:57:31
 */
@Getter
public class NArrayNode extends ArrayNode implements DataType {

    private List<Group> groups = new ArrayList<>();

    public NArrayNode(JsonNodeFactory nf) {
        super(nf);
    }

    public NArrayNode(JsonNodeFactory nf, int capacity) {
        super(nf, capacity);
    }

    @Override
    public byte getTypeCode() {
        return ARRAY;
    }

    @Data
    public static class Group {
        private byte typeCode;
        private ContextType type;
        private int count;
        private boolean end;
    }

}
