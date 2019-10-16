package com.github.sisyphsu.nakedata.proto;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.array.NullArrayNode;
import com.github.sisyphsu.nakedata.node.array.StringArrayNode;
import com.github.sisyphsu.nakedata.node.array.SymbolArrayNode;
import com.github.sisyphsu.nakedata.node.array.primary.*;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-16 19:55:43
 */
public class IOArrayTest {

    private boolean enableCxt;
    private byte[]  bytes;

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

    @Test
    public void testNullArray() throws IOException {
        List list = Arrays.asList(null, null, null, null, null);
        Object result = transIO(new NullArrayNode(list));
        assert Objects.deepEquals(list.toArray(), result);
    }

    @Test
    public void testStringArray() throws IOException {
        List list = Arrays.asList("hello1", "hello2", "hello3", "hello4", "hello5");
        Object result = transIO(new StringArrayNode(list));
        assert Objects.deepEquals(list.toArray(), result);
    }

    @Test
    public void testSymbolArray() throws IOException {
        List list = Arrays.asList("symbol1", "symbol2", "symbol3", "symbol4", "symbol5");

        enableCxt = true;
        Object result = transIO(new SymbolArrayNode(list));
        assert Objects.deepEquals(list.toArray(), result);

        enableCxt = false;
        result = transIO(new SymbolArrayNode(list));
        assert Objects.deepEquals(list.toArray(), result);
    }

    private Object transIO(Node node) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 << 16);
        Output output = new Output(outputStream, enableCxt);
        output.write(node);

        bytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Input input = new Input(inputStream, enableCxt);
        return input.read();
    }

}
