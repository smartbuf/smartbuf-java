package com.github.sisyphsu.canoe.transport;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.github.sisyphsu.canoe.transport.IOTest.enableCxt;
import static com.github.sisyphsu.canoe.transport.IOTest.transIO;

/**
 * @author sulin
 * @since 2019-10-17 14:18:59
 */
public class IOSliceTest {

    @Test
    public void testMixArray() throws IOException {
        List<Object> data = create();
        Object[] srcArr = data.toArray();

        // execute in temporary mode
        enableCxt = false;
        Object tgt = transIO(data);
        assert Objects.deepEquals(srcArr, tgt);

        // execute in context/stream mode
        enableCxt = true;
        tgt = transIO(data);
        assert Objects.deepEquals(srcArr, tgt);
    }

    @Test
    public void test2DArray() throws IOException {
        List<Object> arrs = new ArrayList<>();
        arrs.add(RandomUtils.nextBytes(1024));
        arrs.add(new int[]{0, Integer.MIN_VALUE, Integer.MAX_VALUE});
        arrs.add(new long[]{0, Long.MIN_VALUE, Long.MAX_VALUE});
        arrs.add(new double[]{0, Double.MIN_VALUE, Double.MAX_VALUE});
        arrs.add(new Object[]{Float.MAX_VALUE, Double.MIN_VALUE, null, "hello"});

        List<Object> node = new ArrayList<>();
        node.add(arrs);

        Object[] srcArr = new Object[]{arrs.toArray()};
        Object tgt;
        // execute in temporary mode
        enableCxt = false;
        tgt = transIO(node);
        assert Objects.deepEquals(srcArr, tgt);

        // execute in context/stream mode
        enableCxt = true;
        tgt = transIO(node);
        assert Objects.deepEquals(srcArr, tgt);
    }

    List<Object> create() {
        List<Object> result = new ArrayList<>();
        // boolean
        int len = RandomUtils.nextInt(100, 10000);
        List<Boolean> booleans = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            boolean b = RandomUtils.nextBoolean();
            booleans.add(b);
        }
        result.addAll(booleans);

        // byte
        len = RandomUtils.nextInt(100, 10000);
        List<Byte> bytes = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            byte tmp = (byte) RandomUtils.nextInt();
            bytes.add(tmp);
        }
        result.addAll(bytes);

        // short
        len = RandomUtils.nextInt(100, 10000);
        List<Short> shorts = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            short tmp = (short) RandomUtils.nextInt();
            shorts.add(tmp);
        }
        result.addAll(shorts);

        // int
        len = RandomUtils.nextInt(100, 10000);
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            int tmp = RandomUtils.nextInt();
            ints.add(tmp);
        }
        result.addAll(ints);

        // long
        len = RandomUtils.nextInt(100, 10000);
        List<Long> longs = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            long tmp = RandomUtils.nextLong();
            longs.add(tmp);
        }
        result.addAll(longs);

        // null
        len = RandomUtils.nextInt(100, 10000);
        List<?> nulls = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            nulls.add(null);
        }
        result.addAll(nulls);

        // float
        len = RandomUtils.nextInt(100, 10000);
        List<Float> floats = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            float tmp = RandomUtils.nextFloat();
            floats.add(tmp);
        }
        result.addAll(floats);

        // double
        len = RandomUtils.nextInt(100, 10000);
        List<Double> doubles = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            double tmp = RandomUtils.nextDouble();
            doubles.add(tmp);
        }
        result.addAll(doubles);

        // String
        len = RandomUtils.nextInt(100, 10000);
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            String tmp = RandomStringUtils.random(16);
            strings.add(tmp);
        }
        result.addAll(strings);

        // symbol
        len = RandomUtils.nextInt(100, 10000);
        List<String> symbols = new ArrayList<>();
        for (int i = 0; i < len; i++) {
            String tmp = RandomStringUtils.randomAlphabetic(16);
            symbols.add(tmp);
        }
        result.addAll(symbols);

        return result;
    }

}
