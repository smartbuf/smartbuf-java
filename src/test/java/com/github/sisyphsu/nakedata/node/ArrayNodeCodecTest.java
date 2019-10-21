package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import com.github.sisyphsu.nakedata.node.std.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * @author sulin
 * @since 2019-10-21 11:12:04
 */
public class ArrayNodeCodecTest {
    private ArrayNodeCodec codec = new ArrayNodeCodec();

    @BeforeEach
    void setUp() {
        CodecFactory.Instance.installCodec(BasicNodeCodec.class);
        CodecFactory.Instance.installCodec(ArrayNodeCodec.class);
        CodecFactory.Instance.installCodec(BeanNodeCodec.class);

        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void testNativeArray() {
        Node node;
        ArrayNode.Slice slice;

        boolean[] booleans = new boolean[]{true, false, false};
        node = codec.toNode(booleans);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert Objects.deepEquals(slice.data(), booleans);
        assert Objects.equals(slice.elementType(), SliceType.BOOL_NATIVE);

        byte[] bytes = RandomUtils.nextBytes(15);
        node = codec.toNode(bytes);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert Objects.deepEquals(slice.data(), bytes);
        assert Objects.equals(slice.elementType(), SliceType.BYTE_NATIVE);

        short[] shorts = new short[]{0, Short.MIN_VALUE, Short.MAX_VALUE};
        node = codec.toNode(shorts);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert Objects.deepEquals(slice.data(), shorts);
        assert Objects.equals(slice.elementType(), SliceType.SHORT_NATIVE);

        int[] ints = new int[]{0, Integer.MIN_VALUE, Integer.MAX_VALUE};
        node = codec.toNode(ints);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert Objects.deepEquals(slice.data(), ints);
        assert Objects.equals(slice.elementType(), SliceType.INT_NATIVE);

        long[] longs = new long[]{0, Long.MIN_VALUE, Long.MAX_VALUE};
        node = codec.toNode(longs);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert Objects.deepEquals(slice.data(), longs);
        assert Objects.equals(slice.elementType(), SliceType.LONG_NATIVE);

        float[] floats = new float[]{0, Float.MIN_VALUE, Float.MAX_VALUE};
        node = codec.toNode(floats);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert Objects.deepEquals(slice.data(), floats);
        assert Objects.equals(slice.elementType(), SliceType.FLOAT_NATIVE);

        double[] doubles = new double[]{0, Double.MIN_VALUE, Double.MAX_VALUE};
        node = codec.toNode(doubles);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert Objects.deepEquals(slice.data(), doubles);
        assert Objects.equals(slice.elementType(), SliceType.DOUBLE_NATIVE);

        char[] chars = RandomStringUtils.randomAlphanumeric(16).toCharArray();
        node = codec.toNode(chars);
        assert node instanceof StringNode;
    }

    @Test
    public void testArray() {
        Node node;
        ArrayNode.Slice slice;

        assert codec.toNode(Collections.EMPTY_LIST) == ArrayNode.EMPTY;
        assert codec.toNode(Collections.EMPTY_SET) == ArrayNode.EMPTY;

        List single = Collections.singletonList(null);
        node = codec.toNode(single);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert slice.elementType() == SliceType.NULL;
        slice = ((ArrayNode) node).slices()[0];
        assert slice.elementType() == SliceType.NULL;
        assert slice.asList().size() == 1;

        List nulls = Arrays.asList(new Object[10]);
        node = codec.toNode(nulls);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert slice.elementType() == SliceType.NULL;
        assert Objects.deepEquals(nulls.toArray(), slice.asList().toArray());

        List<Boolean> bools = Arrays.asList(true, false, false);
        node = codec.toNode(bools);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert slice.elementType() == SliceType.BOOL;
        assert Objects.deepEquals(bools.toArray(), slice.asList().toArray());

        List<Byte> bytes = Arrays.asList(Byte.MIN_VALUE, Byte.MAX_VALUE);
        node = codec.toNode(bytes);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert slice.elementType() == SliceType.BYTE;
        assert Objects.deepEquals(bytes.toArray(), slice.asList().toArray());

        List<Short> shorts = Arrays.asList(Short.MIN_VALUE, Short.MAX_VALUE);
        node = codec.toNode(shorts);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert slice.elementType() == SliceType.SHORT;
        assert Objects.deepEquals(shorts.toArray(), slice.asList().toArray());

        List<Integer> ints = Arrays.asList(1, 2, 3, 4, 5, 6);
        node = codec.toNode(ints);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert slice.elementType() == SliceType.INT;
        assert Objects.deepEquals(ints.toArray(), slice.asList().toArray());

        List<Long> longs = Arrays.asList(Long.MIN_VALUE, Long.MAX_VALUE, 0L);
        node = codec.toNode(longs);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert slice.elementType() == SliceType.LONG;
        assert Objects.deepEquals(longs.toArray(), slice.asList().toArray());

        List<Float> floats = Arrays.asList(Float.MIN_VALUE, Float.MAX_VALUE, 0f);
        node = codec.toNode(floats);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert slice.elementType() == SliceType.FLOAT;
        assert Objects.deepEquals(floats.toArray(), slice.asList().toArray());

        List<Double> doubles = Arrays.asList(Double.MIN_VALUE, Double.MAX_VALUE, 0.0);
        node = codec.toNode(doubles);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert slice.elementType() == SliceType.DOUBLE;
        assert Objects.deepEquals(doubles.toArray(), slice.asList().toArray());

        List<String> strings = Arrays.asList("hello", "", "world");
        node = codec.toNode(strings);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert slice.elementType() == SliceType.STRING;
        assert Objects.deepEquals(strings.toArray(), slice.asList().toArray());

        List<Enum> symbols = Arrays.asList(Thread.State.values());
        node = codec.toNode(symbols);
        assert node instanceof ArrayNode;
        assert ((ArrayNode) node).size() == 1;
        slice = ((ArrayNode) node).slices()[0];
        assert slice.elementType() == SliceType.SYMBOL;
        for (int i = 0, len = symbols.size(); i < len; i++) {
            Enum en = symbols.get(i);
            assert en.name().equals(slice.asList().get(i));
        }
    }

    @Test
    public void testMixArray() {
        List<Object> data = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            switch (RandomUtils.nextInt(0, 11)) {
                case 1:
                    boolean b = RandomUtils.nextBoolean();
                    data.add(b);
                    data.add(BooleanNode.valueOf(b));
                    break;
                case 2:
                    data.add((byte) RandomUtils.nextInt());
                    break;
                case 3:
                    data.add((short) RandomUtils.nextInt());
                    break;
                case 4:
                    data.add(RandomUtils.nextInt());
                    break;
                case 5:
                    long l = RandomUtils.nextLong();
                    data.add(l);
                    data.add(VarintNode.valueOf(l));
                    break;
                case 6:
                    float f = RandomUtils.nextFloat();
                    data.add(f);
                    data.add(FloatNode.valueOf(f));
                    break;
                case 7:
                    double d = RandomUtils.nextDouble();
                    data.add(d);
                    data.add(DoubleNode.valueOf(d));
                    break;
                case 8:
                    String str = RandomStringUtils.randomAlphanumeric(16);
                    data.add(str);
                    data.add(StringNode.valueOf(str));
                    break;
                case 9:
                    Enum en = Thread.State.values()[RandomUtils.nextInt(0, 6)];
                    data.add(en);
                    data.add(SymbolNode.valueOf(en));
                    break;
                default:
                    data.add(null);
                    data.add(Optional.empty());
            }
        }
        Node node = codec.toNode(data);
        assert node instanceof ArrayNode;

        ArrayNode.Slice[] slices = ((ArrayNode) node).slices();
        for (ArrayNode.Slice slice : slices) {
            if (slice == null) {
                continue;
            }
            switch (slice.elementType()) {
                case BOOL:
                case LONG:
                case FLOAT:
                case DOUBLE:
                case STRING:
                case SYMBOL:
                    List list = slice.asList();
                    assert list.size() > 1;
                    assert list.size() % 2 == 0;
                    if (!Objects.equals(list.get(0), list.get(1))) {
                        System.out.println();
                    }
                    assert Objects.equals(list.get(0), list.get(1));
                    break;
            }
        }
    }

}
