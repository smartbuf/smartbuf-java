package com.github.smartbuf.converter.codec;

import com.github.smartbuf.converter.Converter;
import com.github.smartbuf.converter.Codec;

import java.nio.*;

/**
 * Codec for java.nio.Buffer, and correlated class
 *
 * @author sulin
 * @since 2019-07-25 17:32:41
 */
public final class BufferCodec extends Codec {

    @Converter
    public byte[] toByteArray(ByteBuffer buf) {
        byte[] arr = new byte[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    @Converter
    public ByteBuffer toByteBuffer(byte[] bs) {
        return ByteBuffer.wrap(bs);
    }

    @Converter
    public char[] toCharArray(CharBuffer buf) {
        char[] arr = new char[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    @Converter
    public CharBuffer toCharBuffer(char[] chars) {
        return CharBuffer.wrap(chars);
    }

    @Converter
    public CharBuffer toCharBuffer(ByteBuffer buf) {
        return buf.asCharBuffer();
    }

    @Converter
    public float[] toFloatArray(FloatBuffer buf) {
        float[] arr = new float[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    @Converter
    public FloatBuffer toFloatBuffer(float[] floats) {
        return FloatBuffer.wrap(floats);
    }

    @Converter
    public FloatBuffer toFloatBuffer(ByteBuffer buf) {
        return buf.asFloatBuffer();
    }

    @Converter
    public double[] toDoubleArray(DoubleBuffer buf) {
        double[] arr = new double[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    @Converter
    public DoubleBuffer toDoubleBuffer(double[] doubles) {
        return DoubleBuffer.wrap(doubles);
    }

    @Converter
    public DoubleBuffer toDoubleBuffer(ByteBuffer buf) {
        return buf.asDoubleBuffer();
    }

    @Converter
    public short[] toShortArray(ShortBuffer buf) {
        short[] arr = new short[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    @Converter
    public ShortBuffer toShortBuffer(short[] shorts) {
        return ShortBuffer.wrap(shorts);
    }

    @Converter
    public ShortBuffer toShortBuffer(ByteBuffer buf) {
        return buf.asShortBuffer();
    }

    @Converter
    public int[] toIntArray(IntBuffer buf) {
        int[] arr = new int[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    @Converter
    public IntBuffer toIntBuffer(int[] ints) {
        return IntBuffer.wrap(ints);
    }

    @Converter
    public IntBuffer toIntBuffer(ByteBuffer buf) {
        return buf.asIntBuffer();
    }

    @Converter
    public long[] toLongArray(LongBuffer buf) {
        long[] arr = new long[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    @Converter
    public LongBuffer toLongBuffer(long[] longs) {
        return LongBuffer.wrap(longs);
    }

    @Converter
    public LongBuffer toLongBuffer(ByteBuffer buf) {
        return buf.asLongBuffer();
    }

}
