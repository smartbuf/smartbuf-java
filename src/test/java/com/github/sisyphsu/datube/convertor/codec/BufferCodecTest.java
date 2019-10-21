package com.github.sisyphsu.datube.convertor.codec;

import com.github.sisyphsu.datube.convertor.CodecFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author sulin
 * @since 2019-08-04 15:36:57
 */
public class BufferCodecTest {

    private BufferCodec codec = new BufferCodec();

    @BeforeEach
    void setUp() {
        codec.setFactory(CodecFactory.Instance);
    }

    @Test
    public void testByteBuffer() {
        String str = "hello world";
        byte[] bytes = str.getBytes();

        ByteBuffer byteBuffer = codec.toByteBuffer(bytes);
        assert Arrays.equals(codec.toByteArray(byteBuffer), bytes);

        assert codec.toCharBuffer(byteBuffer) != null;
        assert codec.toShortBuffer(byteBuffer) != null;
        assert codec.toIntBuffer(byteBuffer) != null;
        assert codec.toLongBuffer(byteBuffer) != null;
        assert codec.toFloatBuffer(byteBuffer) != null;
        assert codec.toDoubleBuffer(byteBuffer) != null;
    }

    @Test
    public void testBuffer() {
        char[] chars = "hello".toCharArray();
        assert Arrays.equals(codec.toCharArray(codec.toCharBuffer(chars)), chars);

        short[] shorts = new short[]{1, 100, 10000};
        assert Arrays.equals(shorts, codec.toShortArray(codec.toShortBuffer(shorts)));

        int[] ints = new int[]{1, 200, 50000};
        assert Arrays.equals(ints, codec.toIntArray(codec.toIntBuffer(ints)));

        long[] longs = new long[]{100L, 100000L, 100000000L};
        assert Arrays.equals(longs, codec.toLongArray(codec.toLongBuffer(longs)));

        float[] floats = new float[]{1.0f, 0.001f, 0.00001f};
        assert Arrays.equals(floats, codec.toFloatArray(codec.toFloatBuffer(floats)));

        double[] doubles = new double[]{1.0, 0.000001, 0.00000000000001};
        assert Arrays.equals(doubles, codec.toDoubleArray(codec.toDoubleBuffer(doubles)));
    }

}
