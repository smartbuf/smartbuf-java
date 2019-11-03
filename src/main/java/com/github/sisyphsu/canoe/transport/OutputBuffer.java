package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.utils.NumberUtils;

import static com.github.sisyphsu.canoe.transport.Const.DATA_ARRAY;

/**
 * @author sulin
 * @since 2019-11-03 16:44:02
 */
public class OutputBuffer {

    private final int limit;

    int    offset;
    byte[] data = new byte[1024];

    public OutputBuffer(int limit) {
        this.limit = limit;
    }

    public void writeByte(byte b) {
        if (data.length == offset) {
            this.ensureCapacity(offset + 1);
        }
        data[offset++] = b;
    }

    public void writeVarInt(long n) {
        this.writeVarUint(NumberUtils.intToUint(n));
    }

    public void writeVarUint(long n) {
        if (data.length < offset + 10) {
            this.ensureCapacity(offset + 10);
        }
        do {
            if ((n & 0xFFFFFFFFFFFFFF80L) == 0) {
                data[offset++] = (byte) n;
            } else {
                data[offset++] = (byte) ((n | 0x80) & 0xFF);
            }
            n >>>= 7;
        } while (n != 0);
    }

    public void writeFloat(float f) {
        if (data.length < offset + 4) {
            this.ensureCapacity(offset + 4);
        }
        int bits = NumberUtils.floatToBits(f);
        for (int i = 0; i < 4; i++) {
            data[offset++] = (byte) (bits & 0xFF);
            bits >>>= 8;
        }
    }

    public void writeDouble(double d) {
        if (data.length < offset + 8) {
            this.ensureCapacity(offset + 8);
        }
        long bits = NumberUtils.doubleToBits(d);
        for (int i = 0; i < 8; i++) {
            data[offset++] = (byte) (bits & 0xFF);
            bits >>>= 8;
        }
    }

    public void writeString(String str) {
        byte[] bytes = str.getBytes();
        if (data.length < offset + bytes.length + 4) {
            this.ensureCapacity(offset + bytes.length + 4);
        }
        this.writeVarUint(bytes.length);
        for (byte b : bytes) {
            data[offset++] = b;
        }
    }

    public void writeBooleanArray(boolean[] arr) {
        int len = arr.length;
        if (data.length < offset + (len + 1) / 8) {
            this.ensureCapacity(offset + (len + 1) / 8);
        }
        int off;
        for (int i = 0; i < len; i += 8) {
            byte b = 0;
            for (int j = 0; j < 8; j++) {
                if ((off = i + j) >= len) {
                    break;
                }
                if (arr[off]) {
                    b |= 1 << j;
                }
            }
            data[offset++] = b;
        }
    }

    public void writeBooleanSlice(Object[] arr, int from, int to) {
        int len = to - from;
        if (data.length < offset + (len + 1) / 8) {
            this.ensureCapacity(offset + (len + 1) / 8);
        }
        int off;
        for (int i = from; i < to; i += 8) {
            byte b = 0;
            for (int j = 0; j < 8; j++) {
                if ((off = i + j) >= len) {
                    break;
                }
                if ((Boolean) arr[off]) {
                    b |= 1 << j;
                }
            }
            data[offset++] = b;
        }
    }

    public void writeByteArray(byte[] arr) {
        if (data.length < offset + arr.length) {
            this.ensureCapacity(offset + arr.length);
        }
        for (byte b : arr) {
            data[offset++] = b;
        }
    }

    public void writeByteSlice(Object[] arr, int from, int to) {
        if (data.length < offset + to - from) {
            this.ensureCapacity(offset + to - from);
        }
        for (int i = from; i < to; i++) {
            data[offset++] = (Byte) arr[i];
        }
    }

    public void writeShortArray(short[] arr) {
        if (data.length < offset + arr.length * 2) {
            this.ensureCapacity(offset + arr.length * 2);
        }
        for (short s : arr) {
            data[offset++] = (byte) (s >> 8);
            data[offset++] = (byte) (s & 0xFF);
        }
    }

    public void writeShortSlice(Object[] arr, int from, int to) {
        if (data.length < offset + (to - from) * 2) {
            this.ensureCapacity(offset + (to - from) * 2);
        }
        for (int i = from; i < to; i++) {
            short s = (Short) arr[i];
            data[offset++] = (byte) (s >> 8);
            data[offset++] = (byte) (s & 0xFF);
        }
    }

    public void writeIntArray(int[] arr) {
        for (int i : arr) {
            writeVarInt(i);
        }
    }

    public void writeIntSlice(Object[] arr, int from, int to) {
        for (int i = from; i < to; i++) {
            writeVarInt((Integer) arr[i]);
        }
    }

    public void writeLongArray(long[] arr) {
        for (long l : arr) {
            writeVarInt(l);
        }
    }

    public void writeLongSlice(Object[] arr, int from, int to) {
        for (int i = from; i < to; i++) {
            writeVarInt((Long) arr[i]);
        }
    }

    public void writeFloatArray(float[] arr) {
        for (float f : arr) {
            writeFloat(f);
        }
    }

    public void writeFloatSlice(Object[] arr, int from, int to) {
        for (int i = from; i < to; i++) {
            writeFloat((Float) arr[i]);
        }
    }

    public void writeDoubleArray(double[] arr) {
        for (double d : arr) {
            writeDouble(d);
        }
    }

    public void writeDoubleSlice(Object[] arr, int from, int to) {
        for (int i = from; i < to; i++) {
            writeDouble((Double) arr[i]);
        }
    }

    public void writeSliceHead(long len, byte type, boolean first, boolean hasMore) {
        if (first) {
            this.writeVarUint((len << 8) | (type << 4) | (hasMore ? 0b0000_1000 : 0) | DATA_ARRAY);
        } else {
            this.writeVarUint((len << 5) | (type << 1) | (hasMore ? 0b0000_0001 : 0));
        }
    }

    public void reset() {
        this.offset = 0;
    }

    private void ensureCapacity(int size) {
        int newSize = Math.min(data.length * 2, limit);
        if (newSize < size) {
            throw new IllegalArgumentException("no space");
        }
        byte[] newData = new byte[newSize];
        System.arraycopy(data, 0, newData, 0, data.length);
        this.data = newData;
    }

}
