package com.github.sisyphsu.nakedata.node.array;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.type.DataType;

import java.util.List;

/**
 * Slice represent an subarray of ArrayNode.
 *
 * @author sulin
 * @since 2019-06-05 11:39:47
 */
public class ArrayNode extends Node {

    public static final ArrayNode NULL = new ArrayNode(null);
    public static final ArrayNode EMPTY = new ArrayNode(null);

    protected List items;

    protected ArrayNode(List items) {
        this.items = items;
    }

    @Override
    public boolean isNull() {
        return false;
    }

    @Override
    public DataType dataType() {
        return DataType.ARRAY;
    }

    public static ArrayNode valueOf(List data) {
        if (data == null) {
            return NilArrayNode.NULL;
        }
        if (data.isEmpty()) {
            return NilArrayNode.EMPTY;
        }
        Object item = data.get(0);
        if (item == null) {
            return new NullArrayNode(data);
        } else if (item instanceof Boolean) {
            return new BooleanArrayNode(data);
        } else if (item instanceof Byte) {
            return new ByteArrayNode(data);
        } else if (item instanceof Short) {
            return new ShortArrayNode(data);
        } else if (item instanceof Integer) {
            return new IntegerArrayNode(data);
        } else if (item instanceof Long) {
            return new LongArrayNode(data);
        } else if (item instanceof Float) {
            return new FloatArrayNode(data);
        } else if (item instanceof Double) {
            return new DoubleArrayNode(data);
        } else if (item instanceof CharSequence) {
            return new StringArrayNode(data);
        } else if (item instanceof Node) {
            return new ObjectArrayNode(data);
        } else {
            throw new IllegalArgumentException("unsupported data: " + item);
        }
    }

    public static MixArrayNode mixOf(List<ArrayNode> list) {
        if (list == null || list.size() < 2) {
            throw new IllegalArgumentException("need multi ArrayNode to build MixArrayNode");
        }
        return new MixArrayNode(list);
    }

    /**
     * Get array's size
     *
     * @return real size
     */
    public int size() {
        return items.size();
    }

    /**
     * Get Array's element dataType
     *
     * @return DataType of element
     */
    public DataType elementType() {
        return null;
    }

    /**
     * Inner ArrayNode, used for specified usecase
     */
    private static class NilArrayNode extends ArrayNode {

        public static final ArrayNode NULL = new NilArrayNode();
        public static final ArrayNode EMPTY = new NilArrayNode();

        private NilArrayNode() {
            super(null);
        }

        @Override
        public int size() {
            return 0;
        }

    }

}
