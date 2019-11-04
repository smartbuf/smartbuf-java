package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.Canoe;
import com.github.sisyphsu.canoe.node.basic.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import sun.net.ProgressSource;

import java.io.IOException;
import java.util.HashMap;
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
        obj = transIO(f);
        assert bytes.length == 2;
        assert Objects.equals(obj, f);

        f = Float.MIN_VALUE;
        obj = transIO(f);
        assert bytes.length == 7;
        assert Objects.equals(obj, f);

        f = Float.MAX_VALUE;
        obj = transIO(f);
        assert bytes.length == 7;
        assert Objects.equals(obj, f);

        f = RandomUtils.nextFloat();
        obj = transIO(f);
        assert bytes.length == 7;
        assert Objects.equals(obj, f);
    }

    @Test
    public void testDouble() throws IOException {
        Object obj;

        Double d = 0.0;
        obj = transIO(d);
        assert bytes.length == 2;
        assert Objects.equals(obj, d);

        d = Double.MIN_VALUE;
        obj = transIO(d);
        assert bytes.length == 11;
        assert Objects.equals(obj, d);

        d = Double.MAX_VALUE;
        obj = transIO(d);
        assert bytes.length == 11;
        assert Objects.equals(obj, d);

        d = RandomUtils.nextDouble();
        obj = transIO(d);
        assert bytes.length == 11;
        assert Objects.equals(obj, d);
    }

    @Test
    public void testVarint() throws IOException {
        Object obj;

        Long l = 0L;
        obj = transIO(l);
        assert bytes.length == 2;
        assert Objects.equals(obj, l);

        l = Long.MIN_VALUE;
        obj = transIO(l);
        assert Objects.equals(obj, l);

        l = Long.MAX_VALUE;
        obj = transIO(l);
        assert Objects.equals(obj, l);

        l = RandomUtils.nextLong();
        obj = transIO(l);
        assert Objects.equals(obj, l);
    }

    @Test
    public void testString() throws IOException {
        Object obj;

        String str = "";
        obj = transIO(str);
        assert bytes.length == 2;
        assert Objects.equals(obj, str);

        str = RandomStringUtils.randomAlphanumeric(10);
        obj = transIO(str);
        assert Objects.equals(obj, str);

        str = RandomStringUtils.random(1000);
        obj = transIO(str);
        assert Objects.equals(obj, str);
    }

    @Test
    public void testSymbol() throws IOException {
        Enum e = ProgressSource.State.DELETE;

        enableCxt = true;
        Object obj = transIO(e);
        assert Objects.equals(e.name(), obj);

        enableCxt = false;
        obj = transIO(e);
        assert Objects.equals(e.name(), obj);
    }

    @Test
    public void testObject() throws IOException {
        Object result = transIO(null);
        assert result == null;
        assert bytes.length == 2;

        result = transIO(new HashMap<>());
        assert bytes.length == 2;
        assert result instanceof Map;
        assert ((Map) result).size() == 0;
    }

    @Test
    public void testError() {
//        Input input = new Input(new ByteArrayInputStream(new byte[]{Const.VER})::read, true);
//        try {
//            input.read();
//            assert false;
//        } catch (Exception e) {
//            assert e instanceof MismatchModeException;
//        }
//
//        input = new Input(new ByteArrayInputStream(new byte[]{Const.VER | Const.VER_STREAM})::read, false);
//        try {
//            input.read();
//            assert false;
//        } catch (Exception e) {
//            assert e instanceof MismatchModeException;
//        }
//
//        input = new Input(new ByteArrayInputStream(new byte[1024])::read, true);
//        try {
//            input.read();
//            assert false;
//        } catch (Exception e) {
//            assert e instanceof InvalidVersionException;
//        }
//
//        try {
//            input.readObject();
//        } catch (Exception e) {
//            assert e instanceof InvalidReadException;
//        }
//
//        try {
//            input.readArray(0b1111_1111L);
//            assert false;
//        } catch (Exception e) {
//            assert e instanceof InvalidReadException;
//        }
//        try {
//            input.readArray(0b1111_1111L);
//            assert false;
//        } catch (Exception e) {
//            assert e instanceof InvalidReadException;
//        }
//        try {
//            input.readPureArray(0xFFL);
//            assert false;
//        } catch (Exception e) {
//            assert e instanceof InvalidReadException;
//        }

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
    static Object transIO(Object o) throws IOException {
        Output output = new Output(Canoe.CODEC, enableCxt);
        bytes = output.write(o);

        Input input = new Input(enableCxt);
        return input.read(bytes);
    }

    public static class Bean {
    }
}
