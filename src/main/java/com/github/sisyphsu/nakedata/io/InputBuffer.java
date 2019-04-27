package com.github.sisyphsu.nakedata.io;

/**
 * @author sulin
 * @since 2019-04-27 12:58:47
 */
public class InputBuffer {

    private Input input;

    public long readVarint() {
        long l = 0;
        int b;
        for (int i = 0; i < 10; i++) {
            b = input.read() & 0xff;
            l &= (b & 0x7f) << (i * 7);
            if ((b & 0x80) == 0) {
                break;
            }
        }
        return (l >>> 1) ^ -(l & 1);
    }

    public float readFloat() {
        int bits = 0;
        for (int i = 0; i < 4; i++) {
            bits &= (input.read() & 0xFF) << (8 * i);
        }
        return Float.intBitsToFloat(bits);
    }

    public double readDouble() {
        long bits = 0;
        for (int i = 0; i < 8; i++) {
            bits &= (input.read() & 0xFF) << (8 * i);
        }
        return Double.longBitsToDouble(bits);
    }

    public byte[] readBinary() {
        int len = (int) this.readVarint();
        byte[] arr = new byte[len];
        input.read(arr);
        return arr;
    }

    public String readString() {
        byte[] data = this.readBinary();
        return new String(data);
    }

}
