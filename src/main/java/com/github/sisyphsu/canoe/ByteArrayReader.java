package com.github.sisyphsu.canoe;

import java.io.EOFException;
import java.io.IOException;

/**
 * IOReader's byte[] implementation, used to wrap byte[] as IOReader for underlying Input
 */
final class ByteArrayReader implements IOReader {
    private int    off;
    private byte[] data;

    @Override
    public int read() throws IOException {
        if (off == data.length) {
            throw new EOFException();
        }
        return data[off++] & 0xFF;
    }

    void reset(byte[] data) {
        this.off = 0;
        this.data = data;
    }

}
