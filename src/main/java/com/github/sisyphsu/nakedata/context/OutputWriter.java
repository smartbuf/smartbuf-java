package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.utils.NumberUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Encapsulate all deserialization operations of output side.
 *
 * @author sulin
 * @since 2019-04-27 13:02:49
 */
public final class OutputWriter {

    private final OutputStream stream;

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
            if ((n & 0xFFFFFFFFFFFFFF80L) == 0) {
                stream.write((byte) n);
            } else {
                stream.write((byte) ((n | 0x80) & 0xFF));
            }
            n >>>= 7;
        } while (n != 0);
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

    public void writeBooleanArray(boolean[] booleans) throws IOException {
        int len = booleans.length;
        int off;
        for (int i = 0; i < len; i += 8) {
            byte b = 0;
            for (int j = 0; j < 8; j++) {
                if ((off = i + j) >= len) {
                    break;
                }
                if (booleans[off]) {
                    b |= 1 << j;
                }
            }
            stream.write(b);
        }
    }

    public void writeByteArray(byte[] bytes) throws IOException {
        for (byte b : bytes) {
            stream.write(b);
        }
    }

    public void writeShortArray(short[] shorts) throws IOException {
        for (short s : shorts) {
            stream.write((byte) (s >> 8));
            stream.write((byte) (s & 0xFF));
        }
    }

    public void writeIntArray(int[] ints) throws IOException {
        for (int i : ints) {
            writeVarInt(i);
        }
    }

    public void writeLongArray(long[] longs) throws IOException {
        for (long l : longs) {
            writeVarInt(l);
        }
    }

    public void writeFloatArray(float[] floats) throws IOException {
        for (float f : floats) {
            writeFloat(f);
        }
    }

    public void writeDoubleArray(double[] doubles) throws IOException {
        for (double d : doubles) {
            writeDouble(d);
        }
    }

    public void writeBooleanSlice(List<Boolean> booleans) throws IOException {
        int len = booleans.size();
        int off;
        for (int i = 0; i < len; i += 8) {
            byte b = 0;
            for (int j = 0; j < 8; j++) {
                if ((off = i + j) >= len) {
                    break;
                }
                if (booleans.get(off)) {
                    b |= 1 << j;
                }
            }
            stream.write(b);
        }
    }

    public void writeByteSlice(List<Byte> bytes) throws IOException {
        for (byte b : bytes) {
            stream.write(b);
        }
    }

    public void writeShortSlice(List<Short> shorts) throws IOException {
        for (short s : shorts) {
            stream.write((byte) (s >>> 8));
            stream.write((byte) (s & 0xFF));
        }
    }

    public void writeIntSlice(List<Integer> ints) throws IOException {
        for (int i : ints) {
            writeVarInt(i);
        }
    }

    public void writeLongSlice(List<Long> longs) throws IOException {
        for (long l : longs) {
            writeVarInt(l);
        }
    }

    public void writeFloatSlice(List<Float> floats) throws IOException {
        for (float f : floats) {
            writeFloat(f);
        }
    }

    public void writeDoubleSlice(List<Double> doubles) throws IOException {
        for (double d : doubles) {
            writeDouble(d);
        }
    }

}
