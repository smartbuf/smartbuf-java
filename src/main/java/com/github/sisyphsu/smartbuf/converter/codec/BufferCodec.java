package com.github.sisyphsu.smartbuf.converter.codec;

import com.github.sisyphsu.smartbuf.converter.Converter;
import com.github.sisyphsu.smartbuf.converter.Codec;

import java.nio.*;

/**
 * Codec for java.nio.Buffer, and correlated class
 *
 * @author sulin
 * @since 2019-07-25 17:32:41
 */
public final class BufferCodec extends Codec {

    /**
     * Convert ByteBuffer to byte[]
     */
    @Converter
    public byte[] toByteArray(ByteBuffer buf) {
        byte[] arr = new byte[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    /**
     * Convert byte[] to ByteBuffer
     */
    @Converter
    public ByteBuffer toByteBuffer(byte[] bs) {
        return ByteBuffer.wrap(bs);
    }

    /**
     * Convert CharBuffer to char[]
     */
    @Converter
    public char[] toCharArray(CharBuffer buf) {
        char[] arr = new char[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    /**
     * Convert char[] to CharBuffer
     */
    @Converter
    public CharBuffer toCharBuffer(char[] chars) {
        return CharBuffer.wrap(chars);
    }

    /**
     * Convert ByteBuffer to CharBuffer
     */
    @Converter
    public CharBuffer toCharBuffer(ByteBuffer buf) {
        return buf.asCharBuffer();
    }

    /**
     * Convert FloatBuffer to float[]
     */
    @Converter
    public float[] toFloatArray(FloatBuffer buf) {
        float[] arr = new float[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    /**
     * Convert float[] to FloatBuffer
     */
    @Converter
    public FloatBuffer toFloatBuffer(float[] floats) {
        return FloatBuffer.wrap(floats);
    }

    /**
     * Convert ByteBuffer to FloatBuffer
     */
    @Converter
    public FloatBuffer toFloatBuffer(ByteBuffer buf) {
        return buf.asFloatBuffer();
    }

    /**
     * Convert DoubleBuffer to double[]
     */
    @Converter
    public double[] toDoubleArray(DoubleBuffer buf) {
        double[] arr = new double[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    /**
     * Convert double[] to DoubleBuffer
     */
    @Converter
    public DoubleBuffer toDoubleBuffer(double[] doubles) {
        return DoubleBuffer.wrap(doubles);
    }

    /**
     * Convert ByteBuffer to DoubleBuffer
     */
    @Converter
    public DoubleBuffer toDoubleBuffer(ByteBuffer buf) {
        return buf.asDoubleBuffer();
    }

    /**
     * Convert ShortBuffer to short[]
     */
    @Converter
    public short[] toShortArray(ShortBuffer buf) {
        short[] arr = new short[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    /**
     * Convert short[] to ShortBuffer
     */
    @Converter
    public ShortBuffer toShortBuffer(short[] shorts) {
        return ShortBuffer.wrap(shorts);
    }

    /**
     * Convert ByteBuffer to ShortBuffer
     */
    @Converter
    public ShortBuffer toShortBuffer(ByteBuffer buf) {
        return buf.asShortBuffer();
    }

    /**
     * Convert IntBuffer to int[]
     */
    @Converter
    public int[] toIntArray(IntBuffer buf) {
        int[] arr = new int[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    /**
     * Convert int[] to IntBuffer
     */
    @Converter
    public IntBuffer toIntBuffer(int[] ints) {
        return IntBuffer.wrap(ints);
    }

    /**
     * Convert ByteBuffer to IntBuffer
     */
    @Converter
    public IntBuffer toIntBuffer(ByteBuffer buf) {
        return buf.asIntBuffer();
    }

    /**
     * Convert LongBuffer to long[]
     */
    @Converter
    public long[] toLongArray(LongBuffer buf) {
        long[] arr = new long[buf.remaining()];
        buf.get(arr, 0, arr.length);
        return arr;
    }

    /**
     * Convert long[] to LongBuffer
     */
    @Converter
    public LongBuffer toLongBuffer(long[] longs) {
        return LongBuffer.wrap(longs);
    }

    /**
     * Convert ByteBuffer to LongBuffer
     */
    @Converter
    public LongBuffer toLongBuffer(ByteBuffer buf) {
        return buf.asLongBuffer();
    }

}
