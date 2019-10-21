package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import com.github.sisyphsu.nakedata.node.std.ArrayNode;
import com.github.sisyphsu.nakedata.node.std.StringNode;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-21 11:12:04
 */
public class ArrayNodeCodecTest {
    private ArrayNodeCodec codec = new ArrayNodeCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void testNative() {
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

    }

}
