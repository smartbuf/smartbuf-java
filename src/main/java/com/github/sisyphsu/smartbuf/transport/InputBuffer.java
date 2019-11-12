package com.github.sisyphsu.smartbuf.transport;

import com.github.sisyphsu.smartbuf.exception.UnexpectedReadException;
import com.github.sisyphsu.smartbuf.utils.NumberUtils;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Encapsulate all deserialize operations of input side.
 *
 * @author sulin
 * @since 2019-11-04 12:00:24
 */
public final class InputBuffer {

    private byte[] data;
    private int    offset;

    /**
     * Reset this buffer, fo reusing
     *
     * @param data New data to switch
     */
    public void reset(byte[] data) {
        this.data = data;
        this.offset = 0;
    }

    public byte readByte() throws IOException {
        if (offset >= data.length) {
            throw new EOFException();
        }
        return data[offset++];
    }

    public short readShort() throws IOException {
        try {
            byte high = data[offset++];
            byte low = data[offset++];
            return (short) ((high << 8) | (low & 0xFF));
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EOFException();
        }
    }

    public long readVarInt() throws IOException {
        long l = this.readVarUint();
        return NumberUtils.uintToInt(l);
    }

    public long readVarUint() throws IOException {
        long l = 0;
        byte b;
        try {
            for (int i = 0; ; i++) {
                b = data[offset++];
                l |= ((long) (b & 0x7F)) << (i * 7);
                if ((b & 0x80) == 0) {
                    break;
                }
                if (i == 10) {
                    throw new UnexpectedReadException("hit invalid varint at " + offset);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EOFException();
        }
        return l;
    }

    public float readFloat() throws IOException {
        int bits = 0;
        int b;
        try {
            for (int i = 0; i < 4; i++) {
                b = data[offset++] & 0xFF;
                bits |= b << (8 * i);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EOFException();
        }
        return NumberUtils.bitsToFloat(bits);
    }

    public double readDouble() throws IOException {
        long bits = 0;
        long b;
        try {
            for (int i = 0; i < 8; i++) {
                b = data[offset++] & 0xFF;
                bits |= b << (8 * i);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EOFException();
        }
        return NumberUtils.bitsToDouble(bits);
    }

    public String readString() throws IOException {
        int len = (int) this.readVarUint();
        if (len > data.length - offset) {
            throw new EOFException();
        }
        try {
            return new String(data, offset, len, StandardCharsets.UTF_8);
        } finally {
            this.offset += len;
        }
    }

    public boolean[] readBooleanArray(int len) throws IOException {
        boolean[] result = new boolean[len];
        try {
            for (int i = 0; i < len; i += 8) {
                byte b = data[offset++];
                for (int j = 0; j < 8; j++) {
                    if (i + j >= len) {
                        break;
                    }
                    result[i + j] = (b & (1 << j)) > 0;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new EOFException();
        }
        return result;
    }

    public byte[] readByteArray(int len) throws IOException {
        if (len > data.length - offset) {
            throw new EOFException();
        }
        byte[] bytes = new byte[len];
        System.arraycopy(data, offset, bytes, 0, len);
        this.offset += len;
        return bytes;
    }

    public short[] readShortArray(int len) throws IOException {
        short[] result = new short[len];
        for (int i = 0; i < len; i++) {
            result[i] = this.readShort();
        }
        return result;
    }

    public int[] readIntArray(int len) throws IOException {
        int[] result = new int[len];
        for (int i = 0; i < len; i++) {
            result[i] = (int) readVarInt();
        }
        return result;
    }

    public long[] readLongArray(int len) throws IOException {
        long[] result = new long[len];
        for (int i = 0; i < len; i++) {
            result[i] = readVarInt();
        }
        return result;
    }

    public float[] readFloatArray(int len) throws IOException {
        float[] result = new float[len];
        for (int i = 0; i < len; i++) {
            result[i] = readFloat();
        }
        return result;
    }

    public double[] readDoubleArray(int len) throws IOException {
        double[] result = new double[len];
        for (int i = 0; i < len; i++) {
            result[i] = readDouble();
        }
        return result;
    }

}
