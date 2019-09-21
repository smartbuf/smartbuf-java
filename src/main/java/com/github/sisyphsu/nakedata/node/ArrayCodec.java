package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.convertor.Codec;
import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.node.array.*;
import com.github.sisyphsu.nakedata.node.array.primary.*;
import com.github.sisyphsu.nakedata.node.std.StringNode;

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
public final class ArrayCodec extends Codec {

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
        return node.getItems();
    }

    @Converter
    public byte[] toArray(BArrayNode node) {
        return node.getItems();
    }

    @Converter
    public short[] toArray(SArrayNode node) {
        return node.getItems();
    }

    @Converter
    public int[] toArray(IArrayNode node) {
        return node.getItems();
    }

    @Converter
    public long[] toArray(LArrayNode node) {
        return node.getItems();
    }

    @Converter
    public float[] toArray(FArrayNode node) {
        return node.getItems();
    }

    @Converter
    public double[] toArray(DArrayNode node) {
        return node.getItems();
    }

    @Converter
    public char[] toArray(StringNode node) {
        return node.getValue().toCharArray();
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
        ArrayNode singleSliceResult = null;
        List<ArrayNode> multiSliceResult = null;
        // iterate list and generate slices.
        Class sliceType = null;
        for (int i = 0, sliceOff = 0, size = list.size(); i < size; i++) {
            Object curr = list.get(i);
            Class type = curr == null ? null : curr.getClass();
            // do nothing for first item
            if (i == 0) {
                sliceType = type;
                continue;
            }
            // continue if continuously and not end
            if (sliceType == type && i < size - 1) {
                continue;
            }
            // generate ArrayNode as slice
            ArrayNode slice;
            List subList = list.subList(sliceOff, i);
            if (sliceType == null) {
                slice = new NullArrayNode(subList);
            } else if (sliceType == Boolean.class) {
                slice = new BooleanArrayNode(subList);
            } else if (sliceType == Byte.class) {
                slice = new ByteArrayNode(subList);
            } else if (sliceType == Short.class) {
                slice = new ShortArrayNode(subList);
            } else if (sliceType == Integer.class) {
                slice = new IntegerArrayNode(subList);
            } else if (sliceType == Long.class) {
                slice = new LongArrayNode(subList);
            } else if (sliceType == Float.class) {
                slice = new FloatArrayNode(subList);
            } else if (sliceType == Double.class) {
                slice = new DoubleArrayNode(subList);
            } else if (sliceType == String.class) {
                slice = new StringArrayNode(subList);
            } else {
                List<Node> nodes = new ArrayList<>(subList.size());
                for (Object o : subList) {
                    nodes.add(convert(o, Node.class));
                }
                slice = new ObjectArrayNode(nodes);
            }
            // update arrays and last
            if (i == size - 1 && multiSliceResult == null) {
                singleSliceResult = slice;
            } else {
                if (multiSliceResult == null) {
                    multiSliceResult = new ArrayList<>();
                }
                multiSliceResult.add(slice);
            }
            // update next slice
            sliceOff = i;
            sliceType = type;
        }
        if (singleSliceResult != null) {
            return singleSliceResult;
        }
        return new MixArrayNode(multiSliceResult);
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
        if (node instanceof MixArrayNode) {
            List result = new ArrayList();
            for (Object item : node.getItems()) {
                result.addAll(((ArrayNode) item).getItems());
            }
            return result.toArray();
        }
        return node.getItems().toArray();
    }

}
