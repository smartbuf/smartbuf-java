package com.github.sisyphsu.nakedata.proto;

import com.github.sisyphsu.nakedata.node.array.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.sisyphsu.nakedata.proto.IOTest.enableCxt;
import static com.github.sisyphsu.nakedata.proto.IOTest.transIO;

/**
 * @author sulin
 * @since 2019-10-17 14:18:59
 */
public class IOSliceTest {

    @Test
    public void testMixArray() throws IOException {
        List<ArrayNode> slices = create();
        List<Object> data = new ArrayList<>();
        for (ArrayNode slice : slices) {
            data.addAll(slice.getItems());
        }
        Object[] srcArr = data.toArray();
        MixArrayNode node = new MixArrayNode(slices);

        // execute in temporary mode
        enableCxt = false;
        Object tgt = transIO(node);
        assert Objects.deepEquals(srcArr, tgt);

        // execute in context/stream mode
        enableCxt = true;
        tgt = transIO(node);
        assert Objects.deepEquals(srcArr, tgt);
    }

    @Test
    public void test2DArray() {
        List<ArrayNode> arr1 = create();
        List<ArrayNode> arr2 = create();
        List<ArrayNode> arr3 = create();

    }

    List<ArrayNode> create() {
        int len;
        List<ArrayNode> slices = new ArrayList<>();
        // boolean
        len = RandomUtils.nextInt(100, 200);
        List<Boolean> booleans = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            boolean b = RandomUtils.nextBoolean();
            booleans.add(b);
        }
        slices.add(ArrayNode.booleanArray(booleans));

        // byte
        len = RandomUtils.nextInt(100, 200);
        List<Byte> bytes = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            byte tmp = (byte) RandomUtils.nextInt();
            bytes.add(tmp);
        }
        slices.add(ArrayNode.byteArray(bytes));

        // short
        len = RandomUtils.nextInt(100, 200);
        List<Short> shorts = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            short tmp = (short) RandomUtils.nextInt();
            shorts.add(tmp);
        }
        slices.add(ArrayNode.shortArray(shorts));

        // int
        len = RandomUtils.nextInt(100, 200);
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            int tmp = RandomUtils.nextInt();
            ints.add(tmp);
        }
        slices.add(ArrayNode.intArray(ints));

        // long
        len = RandomUtils.nextInt(100, 200);
        List<Long> longs = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            long tmp = RandomUtils.nextLong();
            longs.add(tmp);
        }
        slices.add(ArrayNode.longArray(longs));

        // null
        len = RandomUtils.nextInt(100, 200);
        List<?> nulls = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            nulls.add(null);
        }
        slices.add(ArrayNode.nullArray(nulls));

        // float
        len = RandomUtils.nextInt(100, 200);
        List<Float> floats = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            float tmp = RandomUtils.nextFloat();
            floats.add(tmp);
        }
        slices.add(ArrayNode.floatArray(floats));

        // double
        len = RandomUtils.nextInt(100, 200);
        List<Double> doubles = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            double tmp = RandomUtils.nextDouble();
            doubles.add(tmp);
        }
        slices.add(ArrayNode.doubleArray(doubles));

        // String
        len = RandomUtils.nextInt(100, 200);
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            String tmp = RandomStringUtils.random(16);
            strings.add(tmp);
        }
        slices.add(ArrayNode.stringArray(strings));

        // symbol
        len = RandomUtils.nextInt(100, 200);
        List<String> symbols = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            String tmp = RandomStringUtils.randomAlphabetic(16);
            symbols.add(tmp);
        }
        slices.add(ArrayNode.symbolArray(symbols));

        return slices;
    }

}
