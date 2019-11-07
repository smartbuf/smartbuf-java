package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.exception.OutOfSpaceException;
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

    @Test
    public void testOutSpace() {
        OutputBuffer buffer = new OutputBuffer(1 << 10);
        try {
            buffer.writeByteArray(new byte[1025]);
            assert false;
        } catch (Exception e) {
            assert e instanceof OutOfSpaceException;
        }
    }

    @Test
    public void testString2() throws IOException {
        OutputBuffer output = new OutputBuffer(1 << 30);
        InputBuffer input = new InputBuffer();

        output.writeString(RandomStringUtils.randomNumeric(40));
        output.writeString(RandomStringUtils.randomNumeric(5460));
        output.writeString(RandomStringUtils.randomNumeric(699050));

        int size = 1 << 24;
        StringBuilder sb = new StringBuilder(size);
        for (int i = 0; i < size; i++) {
            sb.append(1 % 10);
        }
        output.writeString(sb.toString());

        input.reset(output.data);

        assert input.readString().length() == 40;
        assert input.readString().length() == 5460;
        assert input.readString().length() == 699050;
        assert input.readString().length() == size;
    }

}
