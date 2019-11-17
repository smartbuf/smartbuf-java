package com.github.smartbuf.transport;

import com.github.smartbuf.exception.UnexpectedReadException;
import com.github.smartbuf.utils.NumberUtils;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Encapsulate all deserialize operations of input side.
 *
 * @author sulin
 * @since 2019-11-04 12:00:24
 */
public abstract class InputBuffer {

    public static InputBuffer valueOf(byte[] bytes) {
        return new InputPacketReader(bytes);
    }

    public static InputBuffer valueOf(InputStream is) {
        return new InputStreamReader(is);
    }

    public abstract byte readByte() throws IOException;

    public short readShort() throws IOException {
        byte high = readByte();
        byte low = readByte();
        return (short) ((high << 8) | (low & 0xFF));
    }

    public long readVarInt() throws IOException {
        long l = this.readVarUint();
        return NumberUtils.uintToInt(l);
    }

    public long readVarUint() throws IOException {
        long l = 0;
        for (int i = 0; ; i++) {
            byte b = readByte();
            l |= ((long) (b & 0x7F)) << (i * 7);
            if ((b & 0x80) == 0) {
                break;
            }
            if (i == 10) {
                throw new UnexpectedReadException("hit invalid varint");
            }
        }
        return l;
    }

    public float readFloat() throws IOException {
        int bits = 0;
        int b;
        for (int i = 0; i < 4; i++) {
            b = readByte() & 0xFF;
            bits |= b << (8 * i);
        }
        return NumberUtils.bitsToFloat(bits);
    }

    public double readDouble() throws IOException {
        long bits = 0;
        long b;
        for (int i = 0; i < 8; i++) {
            b = readByte() & 0xFF;
            bits |= b << (8 * i);
        }
        return NumberUtils.bitsToDouble(bits);
    }

    public String readString() throws IOException {
        int len = (int) this.readVarUint();
        byte[] bytes = this.readByteArray(len);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public boolean[] readBooleanArray(int len) throws IOException {
        boolean[] result = new boolean[len];
        for (int i = 0; i < len; i += 8) {
            byte b = readByte();
            for (int j = 0; j < 8; j++) {
                if (i + j >= len) {
                    break;
                }
                result[i + j] = (b & (1 << j)) > 0;
            }
        }
        return result;
    }

    public byte[] readByteArray(int len) throws IOException {
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = readByte();
        }
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

    /**
     * InputReader implementation for byte[]
     */
    private static class InputPacketReader extends InputBuffer {
        private byte[] data;
        private int    offset;

        public InputPacketReader(byte[] data) {
            this.data = data;
            this.offset = 0;
        }

        public byte readByte() throws IOException {
            if (offset >= data.length) {
                throw new EOFException();
            }
            return data[offset++];
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

        public byte[] readByteArray(int len) throws IOException {
            if (len > data.length - offset) {
                throw new EOFException();
            }
            byte[] bytes = new byte[len];
            System.arraycopy(data, offset, bytes, 0, len);
            this.offset += len;
            return bytes;
        }
    }

    /**
     * InputReader implementation for {@link InputStream}
     */
    private static class InputStreamReader extends InputBuffer {
        private InputStream is;

        public InputStreamReader(InputStream is) {
            this.is = is;
        }

        @Override
        public byte readByte() throws IOException {
            int i = is.read();
            if (i == -1) {
                throw new EOFException();
            }
            return (byte) (i & 0xFF);
        }
    }

}
