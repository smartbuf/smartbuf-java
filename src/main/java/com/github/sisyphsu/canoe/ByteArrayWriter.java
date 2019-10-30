package com.github.sisyphsu.canoe;

import com.github.sisyphsu.canoe.exception.OutOfSpaceException;

import java.io.IOException;

/**
 * IOWriter's byte[] implementation, used to accept Output's serialization result
 */
final class ByteArrayWriter implements IOWriter {
    private final int limit;

    private int    off;
    private byte[] data = new byte[1024];

    public ByteArrayWriter(int limit) {
        this.limit = limit;
    }

    @Override
    public void write(byte b) throws IOException {
        if (off >= limit) {
            throw new OutOfSpaceException("hit write limit: " + limit);
        }
        if (off >= data.length) {
            byte[] buf = new byte[data.length * 2];
            System.arraycopy(data, 0, buf, 0, data.length);
            this.data = buf;
        }
        this.data[off++] = b;
    }

    byte[] toByteArray() {
        byte[] result = new byte[off];
        System.arraycopy(data, 0, result, 0, off);
        return result;
    }

    void reset() {
        this.off = 0;
    }
}
