package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.Canoe;
import com.github.sisyphsu.canoe.node.basic.SymbolNode;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-10-21 20:40:21
 */
public class IOFullTest {

    @Test
    public void testOthers() throws IOException {
        IOTest.enableCxt = false;

        Bean bean = new Bean();
        bean.c = 'a';
        bean.chars = "hello".toCharArray();
        bean.node = SymbolNode.valueOf("HELLO");
        Object result = IOTest.transIO(bean);
        Bean newBean = Canoe.CODEC.convert(result, Bean.class);
        assert bean.c == newBean.c;
        assert Arrays.equals(bean.chars, newBean.chars);
        assert Objects.equals(bean.node.value(), newBean.node.value());
    }

    @Data
    public static class Bean {
        private Character  c;
        private char[]     chars;
        private SymbolNode node;
    }

}
