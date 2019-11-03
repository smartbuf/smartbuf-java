package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.node.standard.ArrayNode;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.github.sisyphsu.canoe.transport.IOTest.*;

/**
 * @author sulin
 * @since 2019-10-16 19:55:43
 */
public class IOArrayTest {

    @Test
    public void testZArray() throws IOException {
        boolean[] data = new boolean[RandomUtils.nextInt(100, 200)];
        for (int i = 0; i < data.length; i++) {
            data[i] = RandomUtils.nextInt() % 2 == 0;
        }
        Object result = transIO(ArrayNode.valueOf(data));
        assert Objects.deepEquals(data, result);
    }

    @Test
    public void testBArray() throws IOException {
        byte[] data = RandomUtils.nextBytes(RandomUtils.nextInt(10, 20));
        Object result = transIO(ArrayNode.valueOf(data));

        assert Objects.deepEquals(data, result);
        assert bytes.length == data.length + 3;
    }

    @Test
    public void testSArray() throws IOException {
        short[] data = new short[RandomUtils.nextInt(1000, 2000)];
        for (int i = 0; i < data.length; i++) {
            data[i] = (short) RandomUtils.nextInt();
        }
        Object result = transIO(ArrayNode.valueOf(data));
        assert Objects.deepEquals(result, data);
    }

    @Test
    public void testIArray() throws IOException {
        int[] data = new int[RandomUtils.nextInt(1000, 2000)];
        for (int i = 0; i < data.length; i++) {
            data[i] = RandomUtils.nextInt();
        }
        Object result = transIO(ArrayNode.valueOf(data));
        assert Objects.deepEquals(result, data);
    }

    @Test
    public void testLArray() throws IOException {
        long[] data = new long[RandomUtils.nextInt(1000, 2000)];
        for (int i = 0; i < data.length; i++) {
            data[i] = RandomUtils.nextLong();
        }
        Object result = transIO(ArrayNode.valueOf(data));
        assert Objects.deepEquals(result, data);
    }

    @Test
    public void testFArray() throws IOException {
        float[] data = new float[RandomUtils.nextInt(1000, 2000)];
        for (int i = 0; i < data.length; i++) {
            data[i] = RandomUtils.nextFloat();
        }
        Object result = transIO(ArrayNode.valueOf(data));
        assert Objects.deepEquals(result, data);
    }

    @Test
    public void testDArray() throws IOException {
        double[] data = new double[RandomUtils.nextInt(1000, 2000)];
        for (int i = 0; i < data.length; i++) {
            data[i] = RandomUtils.nextDouble();
        }
        Object result = transIO(ArrayNode.valueOf(data));
        assert Objects.deepEquals(result, data);
    }

    @Test
    public void testNullArray() throws IOException {
        List list = Arrays.asList(null, null, null, null, null);
        ArrayNode node = new ArrayNode();
        node.addNullSlice(list);
        Object result = transIO(node);
        assert Objects.deepEquals(list.toArray(), result);
    }

    @Test
    public void testStringArray() throws IOException {
        List<String> list = Arrays.asList("hello1", "hello2", "hello3", "hello4", "hello5");
        ArrayNode node = new ArrayNode();
        node.addStringSlice(list);
        Object result = transIO(node);
        assert Objects.deepEquals(list.toArray(), result);
    }

    @Test
    public void testEmpty() throws IOException {
        Object result = transIO(ArrayNode.EMPTY);
        assert result instanceof Object[];
        assert ((Object[]) result).length == 0;

        ArrayNode node = new ArrayNode();
        node.addArraySlice(Collections.singletonList(ArrayNode.EMPTY));
        result = transIO(node);
        assert result instanceof Object[];
        assert ((Object[]) result).length == 1;
        assert Objects.deepEquals(((Object[]) result)[0], new Object[0]);
    }

    @Test
    public void testSymbolArray() throws IOException {
        List<String> list = Arrays.asList("symbol1", "symbol2", "symbol3", "symbol4", "symbol5");
        ArrayNode node = new ArrayNode();
        node.addSymbolSlice(list);

        enableCxt = true;
        Object result = transIO(node);
        assert Objects.deepEquals(list.toArray(), result);

        enableCxt = false;
        result = transIO(node);
        assert Objects.deepEquals(list.toArray(), result);
    }

}
