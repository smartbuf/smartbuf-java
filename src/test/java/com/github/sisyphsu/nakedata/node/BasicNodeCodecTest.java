package com.github.sisyphsu.nakedata.node;

import com.github.sisyphsu.nakedata.convertor.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        node = codec.toNode(true);
        assert node.booleanValue();
        node = codec.toNode(false);
        assert !node.booleanValue();

        node = codec.toNode((byte) 0);
        assert node.longValue() == 0L;

        node = codec.toNode((short) 0);
        assert node.longValue() == 0L;

        node = codec.toNode(0);
        assert node.longValue() == 0L;

        node = codec.toNode((long) 0);
        assert node.longValue() == 0L;

        node = codec.toNode((float) 0);
        assert node.floatValue() == 0;

        node = codec.toNode((double) 0);
        assert node.doubleValue() == 0;
    }

}
