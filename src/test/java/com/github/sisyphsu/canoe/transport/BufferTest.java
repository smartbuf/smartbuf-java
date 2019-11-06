package com.github.sisyphsu.canoe.transport;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Objects;

/**
 * @author sulin
 * @since 2019-11-05 18:19:14
 */
public class BufferTest {

    @Test
    public void testString() throws IOException {
        String[] strs = new String[]{
            "hello world",
            "你好，中国",
            "😝😝😝😝😝",
            RandomStringUtils.random(64),
            RandomStringUtils.randomGraph(64)
        };

        OutputBuffer buffer = new OutputBuffer(1 << 20);
        for (String str : strs) {
            buffer.writeString(str);
        }

        InputBuffer inputBuffer = new InputBuffer();
        inputBuffer.reset(buffer.data);

        for (String str : strs) {
            String strInput = inputBuffer.readString();
            System.out.println("# " + str);
            System.out.println("> " + strInput);
            assert Objects.equals(str, strInput);
        }
    }

    @Test
    public void testShort() throws IOException {
        OutputBuffer buffer = new OutputBuffer(1 << 20);
        buffer.writeShort(Short.MIN_VALUE);
        buffer.writeVarUint(Long.MAX_VALUE);
        buffer.writeShort(Short.MAX_VALUE);

        InputBuffer inputBuffer = new InputBuffer();
        inputBuffer.reset(buffer.data);

        assert Short.MIN_VALUE == inputBuffer.readShort();
        assert Long.MAX_VALUE == inputBuffer.readVarUint();
        assert Short.MAX_VALUE == inputBuffer.readShort();
    }
}
