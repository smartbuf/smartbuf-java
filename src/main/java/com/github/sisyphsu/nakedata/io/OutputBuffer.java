package com.github.sisyphsu.nakedata.io;

/**
 * @author sulin
 * @since 2019-04-27 13:02:49
 */
public class OutputBuffer {

    private Output output;

    public int writeVarint(long n) {
        int count = 0;
        for (n = (n << 1) ^ (n >> 63); n > 0; count++, n >>>= 7) {
            if (n <= 0x7F) {
                output.write((byte) n);
            } else {
                output.write((byte) ((n | 0x80) & 0xFF));
            }
        }
        return count;
    }

    public int writeFloat(float f) {
        int bits = Float.floatToRawIntBits(f);
        for (int i = 0; i < 4; i++) {
            output.write((byte) (bits & 0xFF));
            bits >>>= 8;
        }
        return 4;
    }

    public int writeDouble(double d) {
        long bits = Double.doubleToRawLongBits(d);
        for (int i = 0; i < 8; i++) {
            output.write((byte) (bits & 0xFF));
            bits >>>= 8;
        }
        return 8;
    }

    public int writeBinary(byte[] data) {
        int len = this.writeVarint(data.length);
        output.write(data);
        return len + data.length;
    }

    public int writeString(String str) {
        return this.writeBinary(str.getBytes());
    }

}
