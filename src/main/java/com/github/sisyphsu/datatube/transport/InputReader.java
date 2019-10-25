package com.github.sisyphsu.datatube.transport;

import com.github.sisyphsu.datatube.IOReader;
import com.github.sisyphsu.datatube.utils.NumberUtils;

import java.io.EOFException;
import java.io.IOException;

/**
 * Encapsulate all serialization operations of input side.
 *
 * @author sulin
 * @since 2019-10-10 21:44:32
 */
public final class InputReader {

    private final IOReader stream;

    public InputReader(IOReader stream) {
        this.stream = stream;
    }

    public byte readByte() throws IOException {
        int i = stream.read();
        if (i == -1) {
            throw new EOFException();
        }
        return (byte) i;
    }

    public long readVarInt() throws IOException {
        long l = this.readVarUint();
        return NumberUtils.uintToInt(l);
    }

    public long readVarUint() throws IOException {
        long l = 0;
        byte b;
        for (int i = 0; ; i++) {
            b = readByte();
            l |= ((long) (b & 0x7F)) << (i * 7);
            if ((b & 0x80) == 0) {
                break;
            }
            if (i == 10) {
                throw new IOException("invlaid varuint");
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
        int num = (int) this.readVarUint();
        byte[] bytes = new byte[num];
        for (int i = 0; i < num; i++) {
            bytes[i] = readByte();
        }
        return new String(bytes);
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
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = readByte();
        }
        return result;
    }

    public short[] readShortArray(int len) throws IOException {
        short[] result = new short[len];
        for (int i = 0; i < len; i++) {
            byte high = readByte();
            byte low = readByte();
            result[i] = (short) ((high << 8) | (low & 0xFF));
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

    public Boolean[] readBooleanSlice(int len) throws IOException {
        Boolean[] result = new Boolean[len];
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

    public Byte[] readByteSlice(int len) throws IOException {
        Byte[] result = new Byte[len];
        for (int i = 0, off = 0; i < len; i++) {
            result[off++] = readByte();
        }
        return result;
    }

    public Short[] readShortSlice(int len) throws IOException {
        Short[] result = new Short[len];
        for (int i = 0, off = 0; i < len; i++) {
            byte high = readByte();
            byte low = readByte();
            result[off++] = (short) ((high << 8) | (low & 0xFF));
        }
        return result;
    }

    public Integer[] readIntSlice(int len) throws IOException {
        Integer[] result = new Integer[len];
        for (int i = 0, off = 0; i < len; i++) {
            result[off++] = (int) readVarInt();
        }
        return result;
    }

    public Long[] readLongSlice(int len) throws IOException {
        Long[] result = new Long[len];
        for (int i = 0, off = 0; i < len; i++) {
            result[off++] = readVarInt();
        }
        return result;
    }

    public Float[] readFloatSlice(int len) throws IOException {
        Float[] result = new Float[len];
        for (int i = 0, off = 0; i < len; i++) {
            result[off++] = readFloat();
        }
        return result;
    }

    public Double[] readDoubleSlice(int len) throws IOException {
        Double[] result = new Double[len];
        for (int i = 0, off = 0; i < len; i++) {
            result[off++] = readDouble();
        }
        return result;
    }

}
