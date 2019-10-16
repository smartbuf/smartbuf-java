package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.node.Node;
import com.github.sisyphsu.nakedata.node.std.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-16 10:17:06
 */
public class IOSimpleTest {

    private byte[] bytes;

    @Test
    public void testNull() throws IOException {
        for (Node node : Arrays.asList(DoubleNode.NULL, FloatNode.NULL, VarintNode.NULL, StringNode.NULL, ObjectNode.NULL)) {
            Object obj = transIO(node);
            assert bytes.length == 2;
            assert obj == null;
        }
    }

    @Test
    public void testBoolean() throws IOException {
        Object obj = transIO(BooleanNode.valueOf(null));
        assert bytes.length == 2;
        assert obj == null;

        obj = transIO(BooleanNode.valueOf(true));
        assert bytes.length == 2;
        assert Objects.equals(obj, true);

        obj = transIO(BooleanNode.valueOf(false));
        assert bytes.length == 2;
        assert Objects.equals(obj, false);
    }

    @Test
    public void testFloat() throws IOException {
        Object obj = transIO(FloatNode.valueOf(null));
        assert bytes.length == 2;
        assert obj == null;

        Float f = 0f;
        obj = transIO(FloatNode.valueOf(f));
        assert bytes.length == 7;
        assert Objects.equals(obj, f);

        f = Float.MIN_VALUE;
        obj = transIO(FloatNode.valueOf(f));
        assert bytes.length == 7;
        assert Objects.equals(obj, f);

        f = Float.MAX_VALUE;
        obj = transIO(FloatNode.valueOf(f));
        assert bytes.length == 7;
        assert Objects.equals(obj, f);

        f = RandomUtils.nextFloat();
        obj = transIO(FloatNode.valueOf(f));
        assert bytes.length == 7;
        assert Objects.equals(obj, f);
    }

    @Test
    public void testDouble() throws IOException {
        Object obj = transIO(DoubleNode.valueOf(null));
        assert bytes.length == 2;
        assert obj == null;

        Double f = 0.0;
        obj = transIO(DoubleNode.valueOf(f));
        assert bytes.length == 11;
        assert Objects.equals(obj, f);

        f = Double.MIN_VALUE;
        obj = transIO(DoubleNode.valueOf(f));
        assert bytes.length == 11;
        assert Objects.equals(obj, f);

        f = Double.MAX_VALUE;
        obj = transIO(DoubleNode.valueOf(f));
        assert bytes.length == 11;
        assert Objects.equals(obj, f);

        f = RandomUtils.nextDouble();
        obj = transIO(DoubleNode.valueOf(f));
        assert bytes.length == 11;
        assert Objects.equals(obj, f);
    }

    @Test
    public void testVarint() throws IOException {
        Object obj = transIO(VarintNode.valueOf((Long) null));
        assert bytes.length == 2;
        assert obj == null;

        Long l = 0L;
        obj = transIO(VarintNode.valueOf(l));
        assert bytes.length == 4;
        assert Objects.equals(obj, l);

        l = Long.MIN_VALUE;
        obj = transIO(VarintNode.valueOf(l));
        assert Objects.equals(obj, l);

        l = Long.MAX_VALUE;
        obj = transIO(VarintNode.valueOf(l));
        assert Objects.equals(obj, l);

        l = RandomUtils.nextLong();
        obj = transIO(VarintNode.valueOf(l));
        assert Objects.equals(obj, l);
    }

    @Test
    public void testString() throws IOException {
        Object obj = transIO(StringNode.valueOf(null));
        assert bytes.length == 2;
        assert obj == null;

        String str = "";
        obj = transIO(StringNode.valueOf(str));
        assert bytes.length == 4;
        assert Objects.equals(obj, str);

        str = RandomStringUtils.randomAlphanumeric(10);
        obj = transIO(StringNode.valueOf(str));
        assert Objects.equals(obj, str);

        str = RandomStringUtils.random(1000);
        obj = transIO(StringNode.valueOf(str));
        assert Objects.equals(obj, str);
    }

    // exec node -> output -> input -> object
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
