package com.github.sisyphsu.nakedata.io;

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

    public int writeBooleans(boolean[] booleans) {
        return 0;
    }

    public int writeBooleans(List<Boolean> list) {
        return 0;
    }

    public int writeBytes(byte[] bytes) {
        return 0;
    }

    public int writeBytes(List<Byte> bytes) {
        return 0;
    }

    public int writeShorts(short[] shorts) {
        return 0;
    }

    public int writeShorts(List<Short> shorts) {
        return 0;
    }

    public int writeInts(int[] ints) {
        return 0;
    }

    public int writeInts(List<Integer> integers) {
        return 0;
    }

    public int writeLongs(long[] longs) {
        return 0;
    }

    public int writeLongs(List<Long> longs) {
        return 0;
    }

    public int writeFloats(float[] floats) {
        return 0;
    }

    public int writeFloats(List<Float> floats) {
        return 0;
    }

    public int writeDoubles(double[] doubles) {
        return 0;
    }

    public int writeDoubles(List<Double> doubles) {
        return 0;
    }

    public int writeString(String str) {
        byte[] bytes = str.getBytes();
        int len = this.writeVarUint(bytes.length);
        output.write(bytes);
        return len + bytes.length;
    }

}
