package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.exception.InvalidReadException;
import com.github.sisyphsu.canoe.exception.InvalidVersionException;
import com.github.sisyphsu.canoe.exception.MismatchModeException;
import com.github.sisyphsu.canoe.node.Node;
import com.github.sisyphsu.canoe.node.basic.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-16 10:17:06
 */
public class IOTest {

    static boolean enableCxt;
    static byte[]  bytes;

    @Test
    public void testBoolean() throws IOException {
        Object obj;

        obj = transIO(BooleanNode.valueOf(true));
        assert bytes.length == 2;
        assert Objects.equals(obj, true);

        obj = transIO(BooleanNode.valueOf(false));
        assert bytes.length == 2;
        assert Objects.equals(obj, false);
    }

    @Test
    public void testFloat() throws IOException {
        Object obj;

        Float f = 0f;
        obj = transIO(FloatNode.valueOf(f));
        assert bytes.length == 2;
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
        Object obj;

        Double f = 0.0;
        obj = transIO(DoubleNode.valueOf(f));
        assert bytes.length == 2;
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
        Object obj;

        Long l = 0L;
        obj = transIO(VarintNode.valueOf(l));
        assert bytes.length == 2;
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
        Object obj;

        String str = "";
        obj = transIO(StringNode.valueOf(str));
        assert bytes.length == 2;
        assert Objects.equals(obj, str);

        str = RandomStringUtils.randomAlphanumeric(10);
        obj = transIO(StringNode.valueOf(str));
        assert Objects.equals(obj, str);

        str = RandomStringUtils.random(1000);
        obj = transIO(StringNode.valueOf(str));
        assert Objects.equals(obj, str);
    }

    @Test
    public void testSymbol() throws IOException {
        String symbol = "USER_BLOCKED";

        enableCxt = true;
        Object obj = transIO(SymbolNode.valueOf(symbol));
        assert Objects.equals(symbol, obj);

        enableCxt = false;
        obj = transIO(SymbolNode.valueOf(symbol));
        assert Objects.equals(symbol, obj);
    }

    @Test
    public void testObject() throws IOException {
        Object result = transIO(null);
        assert result == null;
        assert bytes.length == 2;

        result = transIO(ObjectNode.EMPTY);
        assert bytes.length == 2;
        assert result instanceof Map;
        assert ((Map) result).size() == 0;
    }

    @Test
    public void testError() {
        Input input = new Input(new ByteArrayInputStream(new byte[]{Const.VER})::read, true);
        try {
            input.read();
            assert false;
        } catch (Exception e) {
            assert e instanceof MismatchModeException;
        }

        input = new Input(new ByteArrayInputStream(new byte[]{Const.VER | Const.VER_STREAM})::read, false);
        try {
            input.read();
            assert false;
        } catch (Exception e) {
            assert e instanceof MismatchModeException;
        }

        input = new Input(new ByteArrayInputStream(new byte[1024])::read, true);
        try {
            input.read();
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidVersionException;
        }

        try {
            input.readNode();
        } catch (Exception e) {
            assert e instanceof InvalidReadException;
        }

        try {
            input.readArray(0b1111_1111L);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidReadException;
        }
        try {
            input.readArray(0b1111_1111L);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidReadException;
        }
        try {
            input.readPureArray(0xFFL);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidReadException;
        }

//        Output output = new Output(null, true);
//        try {
//            output.doWrite(new Node() {
//            }, null);
//            assert false;
//        } catch (Exception e) {
//            assert e instanceof UnsupportedOperationException;
//        }
//
//        try {
//            ArrayNode arrayNode = new ArrayNode();
//            arrayNode.appendSlice(new Object(), 0, SliceType.UNKNOWN);
//            output.doWriteArray(arrayNode, null, false);
//            assert false;
//        } catch (Exception e) {
//            assert e instanceof UnsupportedOperationException;
//        }
    }

    // exec node -> output -> input -> object
    static Object transIO(Node node) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1 << 20);
        Output output = new Output(enableCxt);
        output.write(node);

        bytes = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Input input = new Input(inputStream::read, enableCxt);
        return input.read();
    }

}
