package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.exception.UnexpectReadException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author sulin
 * @since 2019-11-07 18:19:30
 */
public class InputBufferTest {

    @Test
    public void test() {
        InputBuffer buffer = new InputBuffer();

        byte[] bytes = new byte[1024];
        Arrays.fill(bytes, (byte) 0xFF);
        buffer.reset(bytes);

        try {
            buffer.readVarInt();
            assert false;
        } catch (Exception e) {
            assert e instanceof UnexpectReadException;
        }
    }

}
