package com.github.smartbuf.transport;

import com.github.smartbuf.utils.CodecUtils;
import com.github.smartbuf.node.basic.ObjectNode;
import com.github.smartbuf.node.basic.SymbolNode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-11-07 21:39:28
 */
public class IOExpireTest {

    @Test
    public void testSymbolExpire() throws IOException {
        Output.SYMBOL_LIMIT = 4;

        Output output = new Output(true);
        Input input = new Input(true);

        for (int i = 0; i <= Output.SYMBOL_LIMIT + 1; i++) {
            SymbolNode node = SymbolNode.valueOf("SYM" + i);

            byte[] data = output.write(node);
            Object obj = input.read(data);
            assert obj instanceof String;
            assert Objects.equals(obj, node.value());
        }

        // test only DATA_SYMBOL_EXPIRED
        byte[] data = output.write("hello world");
        Object object = input.read(data);
        assert Objects.equals(object, "hello world");
    }

    @Test
    public void testStructExpire() throws IOException {
        Output.STRUCT_LIMIT = 4;

        Output output = new Output(true);
        Input input = new Input(true);

        for (int i = 0; i <= Output.STRUCT_LIMIT + 1; i++) {
            ObjectNode node = new ObjectNode(true, new String[]{"id", "name" + i}, new Object[]{i, "name" + i});

            byte[] data = output.write(node);
            Object obj = input.read(data);

            ObjectNode newNode = CodecUtils.convert(obj, ObjectNode.class);
            assert newNode.keys().length == 2;
            assert newNode.values().length == 2;
        }

        // test only *_EXPIRED
        byte[] data = output.write("hello world");
        Object object = input.read(data);
        assert Objects.equals(object, "hello world");
    }

}
