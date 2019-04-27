package com.github.sisyphsu.nakedata.io;

import com.github.sisyphsu.nakedata.exception.EOFException;
import lombok.Getter;

/**
 * @author sulin
 * @since 2019-04-27 14:35:24
 */
@Getter
public class ByteArrayInput implements Input {

    private int pos;
    private byte[] data;

    public ByteArrayInput(byte[] data) {
        this.data = data;
    }

    @Override
    public byte read() {
        if (remain() < 1) {
            throw new EOFException();
        }
        return data[pos++];
    }

    @Override
    public int read(byte[] bs, int off, int len) {
        if (remain() < len) {
            throw new EOFException();
        }
        System.arraycopy(data, pos, bs, off, len);
        pos += len;
        return len;
    }

    private int remain() {
        return data.length - pos;
    }

}
