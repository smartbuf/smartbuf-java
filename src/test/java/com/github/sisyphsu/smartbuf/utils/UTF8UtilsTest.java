package com.github.sisyphsu.smartbuf.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-11-05 19:53:37
 */
public class UTF8UtilsTest {

    @Test
    public void encode() {
        String[] strs = new String[]{
            "hello world",
            "你好，中国",
            "😝😝😝😝😝",
            RandomStringUtils.random(64),
            RandomStringUtils.randomGraph(64)
        };

        for (String str : strs) {
            byte[] bytes = UTF8Utils.encode(str);
            String newStr = new String(bytes, StandardCharsets.UTF_8);
            assert Objects.equals(str, newStr);
        }
    }

    @Test
    public void testSurrogate() {
        byte[] bytes = UTF8Utils.encode(CharBuffer.wrap(new char[]{0xDDFF}));
        assert bytes[0] == UTF8Utils.REPL;

        bytes = UTF8Utils.encode(CharBuffer.wrap(new char[]{0xDBFF}));
        assert bytes[0] == UTF8Utils.REPL;

        bytes = UTF8Utils.encode(CharBuffer.wrap(new char[]{0xDBFF, 0x00ff}));
        assert bytes[0] == UTF8Utils.REPL;
    }

}
