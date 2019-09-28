package com.github.sisyphsu.nakedata.io;

import com.github.sisyphsu.nakedata.ArrayType;
import com.github.sisyphsu.nakedata.utils.NumberUtils;

import java.util.List;

/**
 * @author sulin
 * @since 2019-04-27 13:02:49
 */
public class OutputWriter {

    private Output output;

    public OutputWriter(Output output) {
        this.output = output;
    }

    public int writeVarInt(long n) {
        n = NumberUtils.intToUint(n);
        return this.writeVarUint(n);
    }

    public int writeByte(byte b) {
        this.output.write(b);
        return 1;
    }

    public int writeShort(short s) {
        output.write((byte) (s & 0xFF));
        output.write((byte) (s >>> 8 & 0xFF));
        return 2;
    }

    public int writeInt(int i) {
        output.write((byte) (i & 0xFF));
        output.write((byte) (i >>> 8 & 0xFF));
        output.write((byte) (i >>> 16 & 0xFF));
        output.write((byte) (i >>> 24 & 0xFF));
        return 4;
    }

    public int writeVarUint(long n) {
        int count = 0;
        do {
            if (n <= 0x7F) {
                output.write((byte) n);
            } else {
                output.write((byte) ((n | 0x80) & 0xFF));
            }
            count++;
            n >>>= 7;
        } while (n > 0);
        return count;
    }

    public int writeFloat(float f) {
        int bits = NumberUtils.floatToBits(f);
        for (int i = 0; i < 4; i++) {
            output.write((byte) (bits & 0xFF));
            bits >>>= 8;
        }
        return 4;
    }

    public int writeDouble(double d) {
        long bits = NumberUtils.doubleToBits(d);
        for (int i = 0; i < 8; i++) {
            output.write((byte) (bits & 0xFF));
            bits >>>= 8;
        }
        return 8;
    }

    public int writeString(String str) {
        byte[] bytes = str.getBytes();
        int len = this.writeVarUint(bytes.length);
        output.write(bytes);
        return len + bytes.length;
    }

    /**
     * Write the metadata of array/slice into output.
     *
     * @param end   Is last slice or not, 1-bit
     * @param type  Element's standard-type, 4-bit
     * @param count Element's count
     */
    public void writeArrayMeta(boolean end, ArrayType type, int count) {

    }

    public void writeBooleanArray(boolean[] booleans) {
    }

    public void writeBooleanArray(List<Boolean> booleans) {
    }

    public void writeByteArray(byte[] bytes) {
    }

    public void writeByteArray(List<Byte> bytes) {
    }

    public void writeShortArray(short[] shorts) {
    }

    public void writeShortArray(List<Short> shorts) {
    }

    public void writeIntArray(int[] ints) {
    }

    public void writeIntArray(List<Integer> integers) {
    }

    public void writeLongArray(long[] longs) {
    }

    public void writeLongArray(List<Long> longs) {
    }

    public void writeFloatArray(float[] floats) {
    }

    public void writeFloatArray(List<Float> floats) {
    }

    public void writeDoubleArray(double[] doubles) {
    }

    public void writeDoubleArray(List<Double> doubles) {
    }

}
