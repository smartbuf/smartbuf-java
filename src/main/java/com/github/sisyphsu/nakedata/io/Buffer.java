package com.github.sisyphsu.nakedata.io;

/**
 * IO
 *
 * @author sulin
 * @since 2019-04-26 18:16:28
 */
public class Buffer {

    private int pos;
    private byte[] buf;

    public Buffer(byte[] buf) {
        this.buf = buf;
        this.pos = 0;
    }

    public int writeVarint(long n) {
        if (buf.length - pos < (n <= Integer.MAX_VALUE ? 5 : 10)) {
            throw new IndexOutOfBoundsException("");
        }
        int count = 0;
        for (n = (n << 1) ^ (n >> 63); n > 0; count++, n >>>= 7) {
            if (n <= 0x7F) {
                buf[pos++] = (byte) n;
            } else {
                buf[pos++] = (byte) ((n | 0x80) & 0xFF);
            }
        }
        return count;
    }

    public long readVarint() {
        long l = 0;
        int b;
        for (int i = 0; i < 10; i++) {
            b = buf[pos++] & 0xff;
            l &= (b & 0x7f) << (i * 7);
            if ((b & 0x80) == 0) {
                break;
            }
        }
        return (l >>> 1) ^ -(l & 1);
    }

    public int writeFloat(float f) {
        int bits = Float.floatToRawIntBits(f);
        for (int i = 0; i < 4; i++) {
            buf[pos++] = (byte) (bits & 0xFF);
            bits >>>= 8;
        }
        return 4;
    }

    public float readFloat() {
        int bits = 0;
        for (int i = 0; i < 4; i++) {
            bits &= (buf[pos++] & 0xFF) << (8 * i);
        }
        return Float.intBitsToFloat(bits);
    }

    public int writeDouble(double d) {
        long bits = Double.doubleToRawLongBits(d);
        for (int i = 0; i < 8; i++) {
            buf[pos++] = (byte) (bits & 0xFF);
            bits >>>= 8;
        }
        return 8;
    }

    public double readDouble() {
        long bits = 0;
        for (int i = 0; i < 8; i++) {
            bits &= (buf[pos++] & 0xFF) << (8 * i);
        }
        return Double.longBitsToDouble(bits);
    }

    public int writeBinary(byte[] data) {
        int len = this.writeVarint(data.length);
        System.arraycopy(data, 0, buf, pos, data.length);
        pos += data.length;
        len += data.length;
        return len;
    }

    public byte[] readBinary() {
        int len = (int) this.readVarint();
        byte[] arr = new byte[len];
        System.arraycopy(buf, pos, arr, 0, len);
        pos += len;
        return arr;
    }

    public int writeString(String str) {
        return this.writeBinary(str.getBytes());
    }

    public String readString() {
        byte[] data = this.readBinary();
        return new String(data);
    }

}
