package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.primary.*;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-16 19:55:43
 */
public class IOArrayTest {

    private byte[] bytes;

    @Test
    public void testZArray() throws IOException {
        boolean[] data = new boolean[RandomUtils.nextInt(100, 200)];
        for (int i = 0; i < data.length; i++) {
            data[i] = RandomUtils.nextInt() % 2 == 0;
        }
        Object result = transIO(ZArrayNode.valueOf(data));
        assert Objects.deepEquals(data, result);
    }

    @Test
    public void testBArray() throws IOException {
        byte[] data = RandomUtils.nextBytes(RandomUtils.nextInt(10, 20));
        Object result = transIO(BArrayNode.valueOf(data));

        assert Objects.deepEquals(data, result);
        assert bytes.length == data.length + 3;
    }

    @Test
    public void testSArray() throws IOException {
        short[] data = new short[RandomUtils.nextInt(1000, 2000)];
        for (int i = 0; i < data.length; i++) {
            data[i] = (short) RandomUtils.nextInt();
        }
        Object result = transIO(SArrayNode.valueOf(data));
        assert Objects.deepEquals(result, data);
    }

    @Test
    public void testIArray() throws IOException {
        int[] data = new int[RandomUtils.nextInt(1000, 2000)];
        for (int i = 0; i < data.length; i++) {
            data[i] = RandomUtils.nextInt();
        }
        Object result = transIO(IArrayNode.valueOf(data));
        assert Objects.deepEquals(result, data);
    }

    @Test
    public void testLArray() throws IOException {
        long[] data = new long[RandomUtils.nextInt(1000, 2000)];
        for (int i = 0; i < data.length; i++) {
            data[i] = RandomUtils.nextLong();
        }
        Object result = transIO(LArrayNode.valueOf(data));
        assert Objects.deepEquals(result, data);
    }

    @Test
    public void testFArray() throws IOException {
        float[] data = new float[RandomUtils.nextInt(1000, 2000)];
        for (int i = 0; i < data.length; i++) {
            data[i] = RandomUtils.nextFloat();
        }
        Object result = transIO(FArrayNode.valueOf(data));
        assert Objects.deepEquals(result, data);
    }

    @Test
    public void testDArray() throws IOException {
        double[] data = new double[RandomUtils.nextInt(1000, 2000)];
        for (int i = 0; i < data.length; i++) {
            data[i] = RandomUtils.nextDouble();
        }
        Object result = transIO(DArrayNode.valueOf(data));
        assert Objects.deepEquals(result, data);
    }

    private Object transIO(Node node) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 << 16);
        Output output = new Output(outputStream, false);
        output.write(node);

        bytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Input input = new Input(inputStream, false);
        return input.read();
    }

}
