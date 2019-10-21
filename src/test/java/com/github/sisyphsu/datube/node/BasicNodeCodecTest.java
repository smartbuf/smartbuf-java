package com.github.sisyphsu.datube.node;

import com.github.sisyphsu.datube.convertor.CodecFactory;
import com.github.sisyphsu.datube.node.std.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-21 11:12:45
 */
public class BasicNodeCodecTest {

    private BasicNodeCodec codec = new BasicNodeCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void test() {
        Node node;

        node = codec.toNode(false);
        assert !node.booleanValue();

        node = codec.toNode(true);
        assert node instanceof BooleanNode;
        assert codec.toBoolean((BooleanNode) node);

        node = codec.toNode((byte) 0);
        assert node instanceof VarintNode;
        assert node.longValue() == 0L;
        assert codec.toByte((VarintNode) node) == (byte) 0;

        node = codec.toNode((short) 0);
        assert node instanceof VarintNode;
        assert node.longValue() == 0L;
        assert codec.toShort((VarintNode) node) == (short) 0;

        node = codec.toNode(0);
        assert node instanceof VarintNode;
        assert node.longValue() == 0L;
        assert codec.toInt((VarintNode) node) == 0;

        node = codec.toNode((long) 0);
        assert node instanceof VarintNode;
        assert node.longValue() == 0L;
        assert codec.toLong((VarintNode) node) == 0L;

        node = codec.toNode((float) 0);
        assert node instanceof FloatNode;
        assert node.floatValue() == 0;
        assert codec.toFloat((FloatNode) node) == 0f;

        node = codec.toNode((double) 0);
        assert node instanceof DoubleNode;
        assert node.doubleValue() == 0;
        assert codec.toDouble((DoubleNode) node) == 0.0;

        node = codec.toNode("");
        assert node == StringNode.EMPTY;
        node = codec.toNode("hello");
        assert node instanceof StringNode;
        assert Objects.equals(node.stringValue(), "hello");
        assert Objects.equals(codec.toString((StringNode) node), "hello");
    }

    @Test
    public void testEnum() {
        final Enum en = Thread.State.BLOCKED;
        Node node;

        node = codec.toNode(en);
        assert node instanceof SymbolNode;
        assert Objects.equals(node.stringValue(), en.name());
        assert Objects.equals(codec.toString((SymbolNode) node), en.name());
    }

}
