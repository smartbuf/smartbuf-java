package com.github.sisyphsu.nakedata.io;

import com.github.sisyphsu.nakedata.context.OutputWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author sulin
 * @since 2019-04-27 14:34:12
 */
public class IOTest {

    @Test
    public void test() {
        byte[] data = new byte[1024];
        OutputWriter output = new OutputWriter(new ByteArrayOutput(data));
        InputReader input = new InputReader(new ByteArrayInput(data));

        output.writeVarInt(0L);
        output.writeVarInt(Byte.MAX_VALUE);
        output.writeVarInt(Byte.MIN_VALUE);
        output.writeVarInt(Short.MAX_VALUE);
        output.writeVarInt(Short.MIN_VALUE);
        output.writeVarInt(Integer.MAX_VALUE);
        output.writeVarInt(Integer.MIN_VALUE);
        output.writeVarInt(Long.MAX_VALUE);
        output.writeVarInt(Long.MIN_VALUE);

        output.writeFloat(0.123f);
        output.writeDouble(0.123456);

        output.writeString("hello world");

        Assertions.assertEquals(input.readVarInt(), 0L);
        Assertions.assertEquals(input.readVarInt(), (long) Byte.MAX_VALUE);
        Assertions.assertEquals(input.readVarInt(), (long) Byte.MIN_VALUE);
        Assertions.assertEquals(input.readVarInt(), (long) Short.MAX_VALUE);
        Assertions.assertEquals(input.readVarInt(), (long) Short.MIN_VALUE);
        Assertions.assertEquals(input.readVarInt(), (long) Integer.MAX_VALUE);
        Assertions.assertEquals(input.readVarInt(), (long) Integer.MIN_VALUE);
        Assertions.assertEquals(input.readVarInt(), Long.MAX_VALUE);
        Assertions.assertEquals(input.readVarInt(), Long.MIN_VALUE);

        Assertions.assertEquals(input.readFloat(), 0.123f);
        Assertions.assertEquals(input.readDouble(), 0.123456);

        Assertions.assertEquals(input.readString(), "hello world");
    }

    @Test
    public void testVarint() {
        long l = Integer.MAX_VALUE;
        long rev = (l << 1) ^ (l >> 63);
        System.out.println(l);
        System.out.println(rev);
        System.out.println((rev >>> 1) ^ -(rev & 1));
    }

}
