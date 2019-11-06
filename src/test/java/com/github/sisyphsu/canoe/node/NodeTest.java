package com.github.sisyphsu.canoe.node;

import com.github.sisyphsu.canoe.node.array.*;
import com.github.sisyphsu.canoe.node.basic.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author sulin
 * @since 2019-09-21 14:10:57
 */
public class NodeTest {

    @Test
    public void testBasic() {
        Node node = StringNode.valueOf("");
        assert node.type() == Node.Type.STRING;

        node = BooleanNode.valueOf(true);
        assert node.type() == Node.Type.BOOLEAN;

        node = DoubleNode.valueOf(111.0);
        assert node.type() == Node.Type.DOUBLE;

        node = FloatNode.valueOf(11.0f);
        assert node.type() == Node.Type.FLOAT;

        node = VarintNode.valueOf(1000);
        assert node.type() == Node.Type.VARINT;

        node = SymbolNode.valueOf(Thread.State.BLOCKED);
        assert node.type() == Node.Type.SYMBOL;

        node = ObjectNode.EMPTY;
        try {
            node.value();
            assert false;
        } catch (Exception e) {
            assert e instanceof UnsupportedOperationException;
        }
    }

    @Test
    public void testArray() {
        Node node = new BooleanArrayNode(new boolean[]{true});
        assert node.type() == Node.Type.ARRAY_BOOLEAN;
        assert node.value() instanceof boolean[];

        node = new ByteArrayNode(new byte[]{1});
        assert node.type() == Node.Type.ARRAY_BYTE;
        assert node.value() instanceof byte[];

        node = new ShortArrayNode(new short[]{1});
        assert node.type() == Node.Type.ARRAY_SHORT;
        assert node.value() instanceof short[];

        node = new IntArrayNode(new int[]{1, 2});
        assert node.type() == Node.Type.ARRAY_INT;
        assert node.value() instanceof int[];

        node = new LongArrayNode(new long[]{1, 2});
        assert node.type() == Node.Type.ARRAY_LONG;
        assert node.value() instanceof long[];

        node = new FloatArrayNode(new float[]{1, 2});
        assert node.type() == Node.Type.ARRAY_FLOAT;
        assert node.value() instanceof float[];

        node = new DoubleArrayNode(new double[]{1, 2});
        assert node.type() == Node.Type.ARRAY_DOUBLE;
        assert node.value() instanceof double[];

        node = new ArrayNode(Arrays.asList(1, 2));
        assert node.type() == Node.Type.ARRAY;
        assert node.value() instanceof List;
    }

}
