package com.github.sisyphsu.nakedata.context;

import com.github.sisyphsu.nakedata.utils.NumberUtils;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author sulin
 * @since 2019-10-10 21:44:32
 */
public class InputReader {

    private final InputStream stream;

    public InputReader(InputStream stream) {
        this.stream = stream;
    }

    public byte readByte() throws IOException {
        int i = stream.read();
        if (i == -1) {
            throw new EOFException();
        }
        return (byte) i;
    }

    public long readVarInt() throws IOException {
        long l = this.readVarUint();
        return NumberUtils.uintToInt(l);
    }

    public long readVarUint() throws IOException {
        long l = 0;
        long b;
        for (int i = 0; i < 10; i++) {
            b = readByte() & 0xFF;
            l |= (b & 0x7F) << (i * 7);
            if ((b & 0x80) == 0) {
                break;
            }
        }
        return l;
    }

    public float readFloat() throws IOException {
        int bits = 0;
        int b;
        for (int i = 0; i < 4; i++) {
            b = readByte() & 0xFF;
            bits |= b << (8 * i);
        }
        return NumberUtils.bitsToFloat(bits);
    }

    public double readDouble() throws IOException {
        long bits = 0;
        long b;
        for (int i = 0; i < 8; i++) {
            b = readByte() & 0xFF;
            bits |= b << (8 * i);
        }
        return NumberUtils.bitsToDouble(bits);
    }

    public String readString() throws IOException {
        long num = this.readVarUint();
        StringBuilder builder = new StringBuilder((int) num);
        for (int i = 0; i < num; i++) {
            builder.append(readByte());
        }
        return builder.toString();
    }

    public boolean[] readBooleanArray() {

        return null;
    }

}
