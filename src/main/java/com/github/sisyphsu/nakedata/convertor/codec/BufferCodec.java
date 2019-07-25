package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;

import java.nio.*;

/**
 * Codec for java.nio.Buffer, and correlated class
 *
 * @author sulin
 * @since 2019-07-25 17:32:41
 */
public class BufferCodec extends Codec {

    /**
     * Convert ByteBuffer to byte[]
     */
    @Converter
    public byte[] toByteArray(ByteBuffer buf) {
        return buf == null ? null : buf.array();
    }

    /**
     * Convert byte[] to ByteBuffer
     */
    @Converter
    public ByteBuffer toByteBuffer(byte[] bs) {
        return bs == null ? null : ByteBuffer.wrap(bs);
    }

    @Converter
    public char[] toCharArray(CharBuffer buf) {
        return buf == null ? null : buf.array();
    }

    @Converter
    public CharBuffer toCharBuffer(char[] chars) {
        return chars == null ? null : CharBuffer.wrap(chars);
    }

    @Converter
    public CharBuffer toCharBuffer(ByteBuffer buf) {
        return buf == null ? null : buf.asCharBuffer();
    }

    @Converter
    public float[] toFloatArray(FloatBuffer buf) {
        return buf == null ? null : buf.array();
    }

    @Converter
    public FloatBuffer toFloatBuffer(float[] floats) {
        return floats == null ? null : FloatBuffer.wrap(floats);
    }

    @Converter
    public FloatBuffer toFloatBuffer(ByteBuffer buf) {
        return buf == null ? null : buf.asFloatBuffer();
    }

    @Converter
    public double[] toDoubleArray(DoubleBuffer buf) {
        return buf == null ? null : buf.array();
    }

    @Converter
    public DoubleBuffer toDoubleBuffer(double[] doubles) {
        return doubles == null ? null : DoubleBuffer.wrap(doubles);
    }

    @Converter
    public DoubleBuffer toDoubleBuffer(ByteBuffer buf) {
        return buf == null ? null : buf.asDoubleBuffer();
    }

    @Converter
    public short[] toShortArray(ShortBuffer buf) {
        return buf == null ? null : buf.array();
    }

    @Converter
    public ShortBuffer toShortBuffer(short[] shorts) {
        return shorts == null ? null : ShortBuffer.wrap(shorts);
    }

    @Converter
    public ShortBuffer toShortBuffer(ByteBuffer buf) {
        return buf == null ? null : buf.asShortBuffer();
    }

    @Converter
    public int[] toIntArray(IntBuffer buf) {
        return buf == null ? null : buf.array();
    }

    @Converter
    public IntBuffer toIntBuffer(int[] ints) {
        return ints == null ? null : IntBuffer.wrap(ints);
    }

    @Converter
    public IntBuffer toIntBuffer(ByteBuffer buf) {
        return buf == null ? null : buf.asIntBuffer();
    }

    @Converter
    public long[] toLongArray(LongBuffer buf) {
        return buf == null ? null : buf.array();
    }

    @Converter
    public LongBuffer toLongBuffer(long[] longs) {
        return longs == null ? null : LongBuffer.wrap(longs);
    }

    @Converter
    public LongBuffer toLongBuffer(ByteBuffer buf) {
        return buf == null ? null : buf.asLongBuffer();
    }

}
