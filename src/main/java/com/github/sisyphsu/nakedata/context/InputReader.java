package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.utils.NumberUtils;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author sulin
 * @since 2019-10-10 21:44:32
 */
public final class InputReader {

    private final InputStream stream;

    public InputReader(InputStream stream) {
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
        for (int i = 0; i < 10; i++) {
            b = readByte();
            l |= ((long) (b & 0x7F)) << (i * 7);
            if ((b & 0x80) == 0) {
                break;
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
        int off;
        for (int i = 0; i < len; i += 8) {
            byte b = readByte();
            for (int j = 0; j < 8; j++) {
                if ((off = i * 8 + j) >= len) {
                    break;
                }
                result[off] = 1 == ((b >>> j) & 1);
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
            result[i] = (short) ((high << 8) | readByte());
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

    public void readBooleanSlice(Object[] arr, int off, int len) throws IOException {
        int pos;
        for (int i = 0; i < len; i += 8) {
            byte b = readByte();
            for (int j = 0; j < 8; j++) {
                if ((pos = i * 8 + j) >= len) {
                    break;
                }
                arr[pos] = ((b >>> j) & 1) == 1;
            }
        }
    }

    public void readByteSlice(Object[] arr, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            arr[off + i] = readByte();
        }
    }

    public void readShortSlice(Object[] arr, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            byte high = readByte();
            arr[off + i] = (short) ((high << 8) | readByte());
        }
    }

    public void readIntSlice(Object[] arr, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            arr[off + i] = (int) readVarInt();
        }
    }

    public void readLongSlice(Object[] arr, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            arr[off + i] = readVarInt();
        }
    }

    public void readFloatSlice(Object[] arr, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            arr[off + i] = readFloat();
        }
    }

    public void readDoubleSlice(Object[] arr, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            arr[off + i] = readDouble();
        }
    }

}
