package com.github.sisyphsu.canoe.node;

import com.github.sisyphsu.canoe.convertor.Codec;
import com.github.sisyphsu.canoe.convertor.Converter;
import com.github.sisyphsu.canoe.node.std.*;

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
            SliceType itemType;
            String[] itemStruct = null;
            if (cls == null) {
                itemType = SliceType.NULL;
            } else if (cls == Boolean.class) {
                itemType = SliceType.BOOL;
            } else if (cls == Byte.class) {
                itemType = SliceType.BYTE;
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
                item = ((Enum) item).name();
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
                    itemStruct = ((ObjectNode) itemNode).keys();
                } else {
                    throw new UnsupportedOperationException("Unsupport node: " + itemNode.getClass());
                }
            }
            arr[offset] = item;
            if (offset == 0) {
                sliceType = itemType;
                sliceStruct = itemStruct;
            }
            boolean hitEnd = offset == arr.length - 1;
            boolean continuous = sliceType == itemType && Objects.deepEquals(sliceStruct, itemStruct);
            List sliceData;
            if (continuous) {
                if (hitEnd) {
                    sliceData = new SubList<>(sliceFrom, arr.length, arr); // no more slice
                    node.appendSlice(sliceData, sliceData.size(), sliceType);
                } else {
                    offset++; // continue for next item
                }
            } else {
                sliceData = new SubList<>(sliceFrom, offset, arr); // may has more slice
                node.appendSlice(sliceData, sliceData.size(), sliceType);
                if (hitEnd) {
                    node.appendSlice(new SubList<>(offset, arr.length, arr), 1, itemType); // handle last item
                } else {
                    sliceFrom = offset++; // prepare for next slice
                    sliceType = itemType;
                    sliceStruct = itemStruct;
                }
            }
        }
        return node;
    }

    /**
     * Convert ArrayNode to Array, in most case, toArray will not copy memory.
     *
     * @param node ArrayNode
     * @return Object[]
     */
    @Converter
    public Object[] toArray(ArrayNode node) {
        if (node == ArrayNode.EMPTY) {
            return new Object[0];
        }
        ArrayNode.Slice[] slices = node.slices();
        if (node.size() == 1) {
            return convert(slices[0].data(), Object[].class);
        }
        int size = 0;
        for (int i = 0, len = node.size(); i < len; i++) {
            size += slices[i].size();
        }
        Object[] result = new Object[size];
        int off = 0;
        for (int i = 0, len = node.size(); i < len; i++) {
            result[off++] = slices[i].asList();
        }
        return result;
    }

}
