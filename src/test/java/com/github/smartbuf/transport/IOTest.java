package com.github.smartbuf.transport;

import com.github.smartbuf.exception.InvalidVersionException;
import com.github.smartbuf.exception.MismatchModeException;
import com.github.smartbuf.exception.UnexpectedReadException;
import com.github.smartbuf.exception.UnexpectedSequenceException;
import com.github.smartbuf.node.Node;
import com.github.smartbuf.node.NodeType;
import com.github.smartbuf.node.array.*;
import com.github.smartbuf.node.basic.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

/**
 * @author sulin
 * @since 2019-10-16 10:17:06
 */
public class IOTest {

    static boolean enableCxt;
    static byte[]  bytes;

    @Test
    public void testOpt() throws IOException {
        Object obj = transIO(OptionalInt.empty());
        assert obj == null;

        obj = transIO("hello world".toCharArray());
        assert "hello world".equals(obj);

        obj = transIO('a');
        assert "a".equals(obj);
    }

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
        Enum e = Thread.State.RUNNABLE;

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
        assert result instanceof ObjectNode;
    }

    @Test
    public void testError() {
        Input input = new Input(true);
        try {
            input.read(new byte[]{Const.VER});
            assert false;
        } catch (Exception e) {
            assert e instanceof MismatchModeException;
        }

        input = new Input(false);
        try {
            input.read(new byte[]{Const.VER | Const.VER_STREAM});
            assert false;
        } catch (Exception e) {
            assert e instanceof MismatchModeException;
        }

        input = new Input(true);

        try {
            input.read(new byte[]{Const.VER | Const.VER_STREAM | Const.VER_HAS_SEQ, 0x02});
            assert false;
        } catch (Exception e) {
            assert e instanceof UnexpectedSequenceException;
        }

        try {
            input.read(new byte[1024]);
            assert false;
        } catch (Exception e) {
            assert e instanceof InvalidVersionException;
        }

        InputBuffer buffer = InputBuffer.valueOf(new byte[1024]);

        try {
            input.readData(buffer);
        } catch (Exception e) {
            assert e instanceof UnexpectedReadException;
        }

        try {
            input.readArray(buffer, 0b1111_1111L);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnexpectedReadException;
        }
        try {
            input.readArray(buffer, 0b1111_1111L);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnexpectedReadException;
        }
        try {
            input.readNativeArray(buffer, 0b1100_0000L);
            assert false;
        } catch (Exception e) {
            assert e instanceof UnexpectedReadException;
        }
    }

    @Test
    public void testNode() throws IOException {
        assert Objects.equals(true, transIO(BooleanNode.valueOf(true)));
        assert Objects.equals(Long.MAX_VALUE, transIO(VarintNode.valueOf(Long.MAX_VALUE)));
        assert Objects.equals(Float.MAX_VALUE, transIO(FloatNode.valueOf(Float.MAX_VALUE)));
        assert Objects.equals(Double.MAX_VALUE, transIO(DoubleNode.valueOf(Double.MAX_VALUE)));
        assert Objects.equals("hello", transIO(StringNode.valueOf("hello")));
        assert Objects.equals("HELLO", transIO(SymbolNode.valueOf("HELLO")));
        assert Objects.deepEquals(new boolean[1], transIO(new BooleanArrayNode(new boolean[1])));
        assert Objects.deepEquals(new byte[1], transIO(new ByteArrayNode(new byte[1])));
        assert Objects.deepEquals(new short[1], transIO(new ShortArrayNode(new short[1])));
        assert Objects.deepEquals(new int[1], transIO(new IntArrayNode(new int[1])));
        assert Objects.deepEquals(new long[1], transIO(new LongArrayNode(new long[1])));
        assert Objects.deepEquals(new float[1], transIO(new FloatArrayNode(new float[1])));
        assert Objects.deepEquals(new double[1], transIO(new DoubleArrayNode(new double[1])));
        assert Objects.deepEquals(new Object[1], transIO(new ArrayNode(Collections.singletonList(null))));

        try {
            transIO(new Node() {
                @Override
                public Object value() {
                    return null;
                }

                @Override
                public NodeType type() {
                    return NodeType.UNKNOWN;
                }
            });
            assert false;
        } catch (Exception e) {
            assert e instanceof IllegalArgumentException;
        }
    }

    // exec node -> output -> input -> object
    static Object transIO(Object o) throws IOException {
        Output output = new Output(enableCxt);
        bytes = output.write(o);

        Input input = new Input(enableCxt);
        return input.read(bytes);
    }
}
