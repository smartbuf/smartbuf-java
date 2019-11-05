package com.github.sisyphsu.canoe.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-11-05 19:53:37
 */
public class UTF8EncoderTest {

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
            byte[] bytes = UTF8Encoder.encode(str);
            String newStr = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("# " + str);
            System.out.println("> " + newStr);
            assert Objects.equals(str, newStr);
        }
    }

}
