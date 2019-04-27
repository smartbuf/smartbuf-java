package com.github.sisyphsu.nakedata.io;

import com.github.sisyphsu.nakedata.utils.AssetUtils;
import org.junit.Test;

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

        output.writeInt(0L);
        output.writeInt(Byte.MAX_VALUE);
        output.writeInt(Byte.MIN_VALUE);
        output.writeInt(Short.MAX_VALUE);
        output.writeInt(Short.MIN_VALUE);
        output.writeInt(Integer.MAX_VALUE);
        output.writeInt(Integer.MIN_VALUE);
        output.writeInt(Long.MAX_VALUE);
        output.writeInt(Long.MIN_VALUE);

        output.writeFloat(0.123f);
        output.writeDouble(0.123456);

        output.writeString("hello world");

        AssetUtils.assetEqual(input.readInt(), 0L);
        AssetUtils.assetEqual(input.readInt(), (long) Byte.MAX_VALUE);
        AssetUtils.assetEqual(input.readInt(), (long) Byte.MIN_VALUE);
        AssetUtils.assetEqual(input.readInt(), (long) Short.MAX_VALUE);
        AssetUtils.assetEqual(input.readInt(), (long) Short.MIN_VALUE);
        AssetUtils.assetEqual(input.readInt(), (long) Integer.MAX_VALUE);
        AssetUtils.assetEqual(input.readInt(), (long) Integer.MIN_VALUE);
        AssetUtils.assetEqual(input.readInt(), Long.MAX_VALUE);
        AssetUtils.assetEqual(input.readInt(), Long.MIN_VALUE);

        AssetUtils.assetEqual(input.readFloat(), 0.123f);
        AssetUtils.assetEqual(input.readDouble(), 0.123456);

        AssetUtils.assetEqual(input.readString(), "hello world");
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