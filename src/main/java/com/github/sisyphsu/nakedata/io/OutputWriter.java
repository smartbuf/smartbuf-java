package com.github.sisyphsu.nakedata.io;

import com.github.sisyphsu.nakedata.utils.NumberUtils;

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

    public int writeBinary(byte[] data) {
        int len = this.writeVarUint(data.length);
        output.write(data);
        return len + data.length;
    }

    public int writeString(String str) {
        return this.writeBinary(str.getBytes());
    }

}
