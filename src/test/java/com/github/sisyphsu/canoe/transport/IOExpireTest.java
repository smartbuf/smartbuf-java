package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.Canoe;
import com.github.sisyphsu.canoe.node.basic.SymbolNode;
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
        int oldLimit = Output.SYMBOL_LIMIT;
        Output.SYMBOL_LIMIT = 4;

        Output output = new Output(Canoe.CODEC, true);
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

        Output.SYMBOL_LIMIT = oldLimit;
    }

}
