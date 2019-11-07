package com.github.sisyphsu.canoe.transport;

import com.github.sisyphsu.canoe.exception.OutOfSpaceException;
import com.github.sisyphsu.canoe.utils.NumberUtils;
import com.github.sisyphsu.canoe.utils.UTF8Utils;

import java.io.IOException;

/**
 * Encapsulate all deserialization operations of output side.
 *
 * @author sulin
 * @since 2019-11-03 16:44:02
 */
public final class OutputBuffer {

    private final int limit;

    int    offset;
    byte[] data = new byte[1024];

    public OutputBuffer(int limit) {
        this.limit = limit;
    }

    /**
     * Reset this buffer, simply reset the offset.
     */
    public void reset() {
        this.offset = 0;
    }

    public void writeByte(byte b) throws IOException {
        if (data.length == offset) {
            this.ensureCapacity(offset + 1);
        }
        data[offset++] = b;
    }

    public void writeShort(short s) throws IOException {
        if (data.length < offset + 2) {
            this.ensureCapacity(offset + 2);
        }
        data[offset++] = (byte) (s >> 8);
        data[offset++] = (byte) (s & 0xFF);
    }

    public void writeVarInt(long n) throws IOException {
        this.writeVarUint(NumberUtils.intToUint(n));
    }

    public int writeVarUint(long n) throws IOException {
        if (data.length < offset + 10) {
            this.ensureCapacity(offset + 10);
        }
        int oldOffset = offset;
        do {
            if ((n & 0xFFFFFFFFFFFFFF80L) == 0) {
                data[offset++] = (byte) n;
            } else {
                data[offset++] = (byte) ((n | 0x80) & 0xFF);
            }
            n >>>= 7;
        } while (n != 0);
        return offset - oldOffset;
    }

    public void writeFloat(float f) throws IOException {
        if (data.length < offset + 4) {
            this.ensureCapacity(offset + 4);
        }
        int bits = NumberUtils.floatToBits(f);
        for (int i = 0; i < 4; i++) {
            data[offset++] = (byte) (bits & 0xFF);
            bits >>>= 8;
        }
    }

    public void writeDouble(double d) throws IOException {
        if (data.length < offset + 8) {
            this.ensureCapacity(offset + 8);
        }
        long bits = NumberUtils.doubleToBits(d);
        for (int i = 0; i < 8; i++) {
            data[offset++] = (byte) (bits & 0xFF);
            bits >>>= 8;
        }
    }

    public void writeString(String str) throws IOException {
        int strLen = str.length();
        // predict the byte number for varuint of utf8-bytes length
        int expectedLen = strLen * 3;
        if (expectedLen < 1 << 7) {
            expectedLen = 1;
        } else if (expectedLen < 1 << 14) {
            expectedLen = 2;
        } else if (expectedLen < 1 << 21) {
            expectedLen = 3;
        } else {
            expectedLen = 5;
        }
        int writeFrom = offset + expectedLen;
        if (data.length < writeFrom + strLen * 3) {
            this.ensureCapacity(writeFrom + strLen * 3);
        }
        int writeTo = UTF8Utils.encode(str, data, writeFrom);
        int len = writeTo - writeFrom;
        if (writeVarUint(len) < expectedLen) {
            System.arraycopy(data, writeFrom, data, offset, len);
        }
        this.offset += len;
    }

    public void writeBooleanArray(boolean[] arr) throws IOException {
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

    public void writeByteArray(byte[] arr) throws IOException {
        int len = arr.length;
        if (data.length < offset + len) {
            this.ensureCapacity(offset + len);
        }
        System.arraycopy(arr, 0, data, offset, len);
        this.offset += len;
    }

    public void writeShortArray(short[] arr) throws IOException {
        if (data.length < offset + arr.length * 2) {
            this.ensureCapacity(offset + arr.length * 2);
        }
        for (short s : arr) {
            data[offset++] = (byte) (s >> 8);
            data[offset++] = (byte) (s & 0xFF);
        }
    }

    public void writeIntArray(int[] arr) throws IOException {
        for (int i : arr) {
            writeVarInt(i);
        }
    }

    public void writeLongArray(long[] arr) throws IOException {
        for (long l : arr) {
            writeVarInt(l);
        }
    }

    public void writeFloatArray(float[] arr) throws IOException {
        for (float f : arr) {
            writeFloat(f);
        }
    }

    public void writeDoubleArray(double[] arr) throws IOException {
        for (double d : arr) {
            writeDouble(d);
        }
    }

    private void ensureCapacity(int size) throws IOException {
        int newSize = Math.min(Math.max(data.length * 2, size), limit);
        if (newSize < size) {
            throw new OutOfSpaceException("no space");
        }
        byte[] newData = new byte[newSize];
        System.arraycopy(data, 0, newData, 0, data.length);
        this.data = newData;
    }

}
