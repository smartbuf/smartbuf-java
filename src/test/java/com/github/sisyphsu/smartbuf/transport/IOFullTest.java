package com.github.sisyphsu.smartbuf.transport;

import com.github.sisyphsu.smartbuf.utils.CodecUtils;
import com.github.sisyphsu.smartbuf.node.basic.SymbolNode;
import lombok.Data;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
        Bean newBean = CodecUtils.convert(result, Bean.class);
        assert bean.c == newBean.c;
        assert Arrays.equals(bean.chars, newBean.chars);
        assert Objects.equals(bean.node.value(), newBean.node.value());
    }

    @Test
    public void testMap() throws IOException {
        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("name", "hello");
        IOTest.transIO(map);
    }

    @Data
    public static class Bean {
        private Character    c;
        private char[]       chars;
        private SymbolNode   node;
        private double       double1;
        private Double       double2;
        private Thread.State state = Thread.State.BLOCKED;
    }

}
