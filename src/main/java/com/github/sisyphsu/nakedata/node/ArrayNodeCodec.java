package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.node.std.ArrayNode;
import com.github.sisyphsu.nakedata.node.std.StringNode;
import com.github.sisyphsu.nakedata.node.std.primary.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Array, Collection's codec implemention.
 * <p>
 * Primary array can wrapped as specified Node directly, for better performance.
 *
 * @author sulin
 * @since 2019-06-05 19:53:57
 */
@SuppressWarnings("unchecked")
public final class ArrayNodeCodec extends Codec {

    @Converter
    public Node toNode(boolean[] arr) {
        return ZArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(byte[] arr) {
        return BArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(int[] arr) {
        return IArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(long[] arr) {
        return LArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(short[] arr) {
        return SArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(float[] arr) {
        return FArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(double[] arr) {
        return DArrayNode.valueOf(arr);
    }

    @Converter
    public Node toNode(char[] arr) {
        return StringNode.valueOf(String.valueOf(arr));
    }

    @Converter
    public boolean[] toArray(ZArrayNode node) {
        return node.booleansValue();
    }

    @Converter
    public byte[] toArray(BArrayNode node) {
        return node.bytesValue();
    }

    @Converter
    public short[] toArray(SArrayNode node) {
        return node.shortsValue();
    }

    @Converter
    public int[] toArray(IArrayNode node) {
        return node.intsValue();
    }

    @Converter
    public long[] toArray(LArrayNode node) {
        return node.longsValue();
    }

    @Converter
    public float[] toArray(FArrayNode node) {
        return node.floatsValue();
    }

    @Converter
    public double[] toArray(DArrayNode node) {
        return node.doublesValue();
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
    public Node toNode(List list) {
        if (list == null) {
            return ArrayNode.NULL;
        }
        if (list.isEmpty()) {
            return ArrayNode.EMPTY;
        }
        ArrayNode node = new ArrayNode();
        // iterate list and generate slices.
        Class sliceType = null;
        int sliceOff = 0;
        for (int off = 0, size = list.size(); off < size; off++) {
            Object curr = list.get(off);
            Class type = curr == null ? null : curr.getClass();
            // do nothing for first item
            if (off == 0) {
                sliceType = type;
                continue;
            }
            // continue if continuously and not end
            if (sliceType == type && off < size - 1) {
                continue;
            }
            // generate ArrayNode as slice
            List subList = list.subList(sliceOff, off + 1);
            if (sliceType == null) {
                node.addNullSlice(subList);
            } else if (sliceType == Boolean.class) {
                node.addBooleanSlice(subList);
            } else if (sliceType == Byte.class) {
                node.addByteSlice(subList);
            } else if (sliceType == Short.class) {
                node.addShortSlice(subList);
            } else if (sliceType == Integer.class) {
                node.addIntSlice(subList);
            } else if (sliceType == Long.class) {
                node.addLongSlice(subList);
            } else if (sliceType == Float.class) {
                node.addFloatSlice(subList);
            } else if (sliceType == Double.class) {
                node.addDoubleSlice(subList);
            } else if (String.class.isAssignableFrom(sliceType)) {
                node.addStringSlice(subList);
            } else if (Enum.class.isAssignableFrom(sliceType)) {
                node.addSymbolSlice(subList); // subList's component shouldn't be string
            } else {
                List<Node> nodes = new ArrayList<>(subList.size());
                for (Object o : subList) {
                    nodes.add(convert(o, Node.class));
                }
                // TODO 此时不一定是ObjectNode，需要根据Node具体类型再次分组？
                node.addNodeArray(nodes);
            }
            // update next slice
            sliceOff = off;
            sliceType = type;
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
    public Object[] toList(ArrayNode node) {
        if (node == ArrayNode.NULL) {
            return null;
        }
        if (node == ArrayNode.EMPTY) {
            return new Object[0];
        }
        List<ArrayNode.Slice> slices = node.getSlices();
        switch (slices.size()) {
            case 0:
                return new Object[0];
            case 1:
                return slices.get(0).getItems().toArray();
            default:
                List result = new ArrayList();
                for (ArrayNode.Slice slice : slices) {
                    result.addAll(slice.getItems());
                }
                return result.toArray();
        }
    }

}
