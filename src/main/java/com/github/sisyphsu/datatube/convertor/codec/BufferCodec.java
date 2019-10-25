package com.github.sisyphsu.datatube.convertor.codec;

import com.github.sisyphsu.datatube.convertor.Converter;
import com.github.sisyphsu.datatube.convertor.Codec;

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
        return buf.array();
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
        return buf.array();
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
        return buf.array();
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
        return buf.array();
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
        return buf.array();
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
        return buf.array();
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
        return buf.array();
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
