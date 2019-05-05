package com.github.sisyphsu.nakedata.io;

import com.github.sisyphsu.nakedata.utils.NumberUtils;

/**
 * @author sulin
 * @since 2019-04-27 12:58:47
 */
public class InputReader {

    private Input input;

    public InputReader(Input input) {
        this.input = input;
    }

    public byte readByte() {
        return input.read();
    }

    public int readInt24() {
        int i = input.read();
        i |= input.read() << 8;
        i |= input.read() << 16;
        return i;
    }

    public int readInt32() {
        int i = input.read();
        i |= input.read() << 8;
        i |= input.read() << 16;
        i |= input.read() << 24;
        return i;
    }

    public long readVarInt() {
        long l = this.readVarUint();
        return NumberUtils.uintToInt(l);
    }

    public long readVarUint() {
        long l = 0;
        long b;
        for (int i = 0; i < 10; i++) {
            b = input.read() & 0xff;
            l |= (b & 0x7f) << (i * 7);
            if ((b & 0x80) == 0) {
                break;
            }
        }
        return l;
    }

    public float readFloat() {
        int bits = 0;
        int b;
        for (int i = 0; i < 4; i++) {
            b = input.read() & 0xFF;
            bits |= b << (8 * i);
        }
        return NumberUtils.bitsToFloat(bits);
    }

    public double readDouble() {
        long bits = 0;
        long b;
        for (int i = 0; i < 8; i++) {
            b = input.read() & 0xFF;
            bits |= b << (8 * i);
        }
        return NumberUtils.bitsToDouble(bits);
    }

    public byte[] readBinary() {
        int len = (int) this.readVarUint();
        byte[] arr = new byte[len];
        input.read(arr);
        return arr;
    }

    public String readString() {
        byte[] data = this.readBinary();
        return new String(data);
    }

}
