package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.node.std.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Array, Collection's codec implemention.
 * <p>
 * Primary array can wrapped as specified Node directly, for better performance.
 *
 * @author sulin
 * @since 2019-06-05 19:53:57
 */
public final class ArrayNodeCodec extends Codec {

    @Converter
    public Node toNode(boolean[] arr) {
        return ArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(byte[] arr) {
        return ArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(int[] arr) {
        return ArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(long[] arr) {
        return ArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(short[] arr) {
        return ArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(float[] arr) {
        return ArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(double[] arr) {
        return ArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(char[] arr) {
        return StringNode.valueOf(String.valueOf(arr));
    }

    @Converter
    public char[] toArray(StringNode node) {
        return node.stringValue().toCharArray();
    }

    /**
     * Encode List to ArrayNode, all Object[] and Collection should be encode through List.
     *
     * @param list List
     * @return ArrayNode
     */
    @Converter
    public Node toNode(Collection list) {
        if (list.isEmpty()) {
            return ArrayNode.EMPTY;
        }
        final ArrayNode node = new ArrayNode();
        final Object[] arr = new Object[list.size()];
        // trim collection to native array which has multiple slices
        int offset = 0;
        int sliceFrom = 0;
        SliceType sliceType = null;
        String[] sliceStruct = null;
        for (Object item : list) {
            Class cls = (item == null) ? null : item.getClass();
            SliceType itemType = null;
            String[] itemStruct = null;
            if (cls == null) {
                itemType = SliceType.NULL;
            } else if (cls == Boolean.class) {
                itemType = SliceType.BOOL;
            } else if (cls == Short.class) {
                itemType = SliceType.SHORT;
            } else if (cls == Integer.class) {
                itemType = SliceType.INT;
            } else if (cls == Long.class) {
                itemType = SliceType.LONG;
            } else if (cls == Float.class) {
                itemType = SliceType.FLOAT;
            } else if (cls == Double.class) {
                itemType = SliceType.DOUBLE;
            } else if (CharSequence.class.isAssignableFrom(cls)) {
                item = String.valueOf(item);
                itemType = SliceType.STRING;
            } else if (Enum.class.isAssignableFrom(cls)) {
                itemType = SliceType.SYMBOL;
            } else {
                Node itemNode = convert(item, Node.class);
                if (itemNode == null) {
                    item = null;
                    itemType = SliceType.NULL;
                } else if (itemNode instanceof BooleanNode) {
                    item = itemNode.booleanValue();
                    itemType = SliceType.BOOL;
                } else if (itemNode instanceof VarintNode) {
                    item = itemNode.longValue();
                    itemType = SliceType.LONG;
                } else if (itemNode instanceof FloatNode) {
                    item = itemNode.floatValue();
                    itemType = SliceType.FLOAT;
                } else if (itemNode instanceof DoubleNode) {
                    item = itemNode.doubleValue();
                    itemType = SliceType.DOUBLE;
                } else if (itemNode instanceof StringNode) {
                    item = itemNode.stringValue();
                    itemType = SliceType.STRING;
                } else if (itemNode instanceof SymbolNode) {
                    item = itemNode.stringValue();
                    itemType = SliceType.SYMBOL;
                } else if (itemNode instanceof ArrayNode) {
                    item = itemNode;
                    itemType = SliceType.ARRAY;
                } else if (itemNode instanceof ObjectNode) {
                    item = itemNode;
                    itemType = SliceType.OBJECT;
                    itemStruct = ((ObjectNode) itemNode).getFields();
                }
            }
            arr[offset++] = item;
            // check continuous
            boolean continuous = sliceType == itemType && Objects.deepEquals(sliceStruct, itemStruct);
            if (continuous && offset < arr.length) {
                continue;
            }
            // create previous slice
            List sliceData;
            if (arr.length == 1) {
                sliceData = new SubList<>(0, 1, arr);
            } else if (continuous) {
                sliceData = new SubList<>(sliceFrom, offset - sliceFrom, arr);// no more slice
                sliceType = itemType;
            } else {
                sliceData = new SubList<>(sliceFrom, offset - 1 - sliceFrom, arr);// has more slice
            }
            node.appendSlice(sliceData, sliceData.size(), sliceType);
            // prepare next slice
            sliceFrom = offset;
            sliceType = itemType;
            sliceStruct = itemStruct;
        }
        return node;
    }

}
