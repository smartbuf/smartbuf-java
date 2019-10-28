package com.github.sisyphsu.canoe;

import com.github.sisyphsu.canoe.exception.OutOfSpaceException;

import java.io.IOException;

/**
 * IOWriter's byte[] implementation, used to accept Output's serialization result
 */
final class ByteArrayWriter implements IOWriter {
    private final int limit;

    private int      off;
    private byte[][] data = new byte[64][];

    public ByteArrayWriter(int limit) {
        this.limit = limit;
    }

    @Override
    public void write(byte b) throws IOException {
        if (off >= limit) {
            throw new OutOfSpaceException("hit write limit: " + limit);
        }
        int pos = off++;
        int sliceOff = pos / 1024;
        if (sliceOff >= data.length) {
            byte[][] newData = new byte[data.length * 2][];
            System.arraycopy(data, 0, newData, 0, data.length);
            this.data = newData;
        }
        if (data[sliceOff] == null) {
            data[sliceOff] = new byte[1024];
        }
        data[sliceOff][pos % 1024] = b;
    }

    byte[] toByteArray() {
        byte[] result = new byte[off];
        for (int i = 0; i < off; i += 1024) {
            byte[] slice = data[i / 1024];
            int sliceLen = Math.min(off - i, 1024);
            System.arraycopy(slice, 0, result, i, sliceLen);
        }
        return result;
    }

    void reset() {
        this.off = 0;
    }
}
