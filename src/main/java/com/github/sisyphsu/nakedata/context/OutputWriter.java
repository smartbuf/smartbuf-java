package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.ArrayType;
import com.github.sisyphsu.nakedata.utils.NumberUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.BitSet;
import java.util.List;

/**
 * @author sulin
 * @since 2019-04-27 13:02:49
 */
public class OutputWriter {

    private OutputStream stream;

    public OutputWriter(OutputStream stream) {
        this.stream = stream;
    }

    public void writeByte(byte b) throws IOException {
        stream.write(b);
    }

    public void writeVarInt(long n) throws IOException {
        this.writeVarUint(NumberUtils.intToUint(n));
    }

    public void writeVarUint(long n) throws IOException {
        do {
            if (n <= 0x7F) {
                stream.write((byte) n);
            } else {
                stream.write((byte) ((n | 0x80) & 0xFF));
            }
            n >>>= 7;
        } while (n > 0);
    }

    public void writeFloat(float f) throws IOException {
        int bits = NumberUtils.floatToBits(f);
        for (int i = 0; i < 4; i++) {
            stream.write((byte) (bits & 0xFF));
            bits >>>= 8;
        }
    }

    public void writeDouble(double d) throws IOException {
        long bits = NumberUtils.doubleToBits(d);
        for (int i = 0; i < 8; i++) {
            stream.write((byte) (bits & 0xFF));
            bits >>>= 8;
        }
    }

    public void writeString(String str) throws IOException {
        byte[] bytes = str.getBytes();
        this.writeVarUint(bytes.length);
        for (byte b : bytes) {
            stream.write(b);
        }
    }

    public void writeNullSlice(List booleans, boolean end) throws IOException {
        writeVarUint((booleans.size() << 5) | (ArrayType.NULL.getCode() << 1) | (end ? 0 : 1));
    }

    public void writeBooleanArray(boolean[] booleans) throws IOException {
        int len = booleans.length;
        BitSet set = new BitSet();
        for (int i = 0; i < len; i++) {
            set.set(i, booleans[i]);
        }
        writeVarUint((len << 5) | (ArrayType.BOOL.getCode() << 1));
        for (byte b : set.toByteArray()) {
            stream.write(b);
        }
    }

    public void writeBooleanSlice(List<Boolean> booleans, boolean end) throws IOException {
        int len = booleans.size();
        BitSet set = new BitSet();
        for (int i = 0; i < len; i++) {
            set.set(i, booleans.get(i));
        }
        writeVarUint((len << 5) | (ArrayType.BOOL.getCode() << 1) | (end ? 0 : 1));
        for (byte b : set.toByteArray()) {
            stream.write(b);
        }
    }

    public void writeByteArray(byte[] bytes) throws IOException {
        int len = bytes.length;
        writeVarUint((len << 5) | (ArrayType.BYTE.getCode() << 1));
        for (byte b : bytes) {
            stream.write(b);
        }
    }

    public void writeByteSlice(List<Byte> bytes, boolean end) throws IOException {
        int len = bytes.size();
        writeVarUint((len << 5) | (ArrayType.BYTE.getCode() << 1) | (end ? 0 : 1));
        for (byte b : bytes) {
            stream.write(b);
        }
    }

    public void writeShortArray(short[] shorts) throws IOException {
        int len = shorts.length;
        writeVarUint((len << 5) | (ArrayType.SHORT.getCode() << 1));
        for (short s : shorts) {
            stream.write((byte) (s >> 8));
            stream.write((byte) s);
        }
    }

    public void writeShortSlice(List<Short> shorts, boolean end) throws IOException {
        int len = shorts.size();
        writeVarUint((len << 5) | (ArrayType.SHORT.getCode() << 1) | (end ? 0 : 1));
        for (short s : shorts) {
            stream.write((byte) (s >> 8));
            stream.write((byte) s);
        }
    }

    public void writeIntArray(int[] ints) throws IOException {
        int len = ints.length;
        writeVarUint((len << 5) | (ArrayType.INT.getCode() << 1));
        for (int i : ints) {
            writeVarInt(i);
        }
    }

    public void writeIntSlice(List<Integer> ints, boolean end) throws IOException {
        int len = ints.size();
        writeVarUint((len << 5) | (ArrayType.INT.getCode() << 1) | (end ? 0 : 1));
        for (int i : ints) {
            writeVarInt(i);
        }
    }

    public void writeLongArray(long[] longs) throws IOException {
        int len = longs.length;
        writeVarUint((len << 5) | (ArrayType.LONG.getCode() << 1));
        for (long l : longs) {
            writeVarInt(l);
        }
    }

    public void writeLongArray(List<Long> longs, boolean end) throws IOException {
        int len = longs.size();
        writeVarUint((len << 5) | (ArrayType.LONG.getCode() << 1) | (end ? 0 : 1));
        for (long l : longs) {
            writeVarInt(l);
        }
    }

    public void writeFloatArray(float[] floats) throws IOException {
        int len = floats.length;
        writeVarUint((len << 5) | (ArrayType.FLOAT.getCode() << 1) | 1);
        for (float f : floats) {
            writeFloat(f);
        }
    }

    public void writeFloatArray(List<Float> floats, boolean end) throws IOException {
        int len = floats.size();
        writeVarUint((len << 5) | (ArrayType.FLOAT.getCode() << 1) | (end ? 0 : 1));
        for (float f : floats) {
            writeFloat(f);
        }
    }

    public void writeDoubleArray(double[] doubles) throws IOException {
        int len = doubles.length;
        writeVarUint((len << 5) | (ArrayType.DOUBLE.getCode() << 1));
        for (double d : doubles) {
            writeDouble(d);
        }
    }

    public void writeDoubleArray(List<Double> doubles, boolean end) throws IOException {
        int len = doubles.size();
        writeVarUint((len << 5) | (ArrayType.DOUBLE.getCode() << 1) | (end ? 0 : 1));
        for (double d : doubles) {
            writeDouble(d);
        }
    }

    public void writeMetaHead(long size, int code, boolean hasMore) throws IOException {
        this.writeVarUint((size << 4) | (code << 1) | (hasMore ? 1 : 0));
    }

}
