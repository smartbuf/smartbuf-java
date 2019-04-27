package com.github.sisyphsu.nakedata.io;

import com.github.sisyphsu.nakedata.exception.EOFException;
import lombok.Getter;

/**
 * @author sulin
 * @since 2019-04-27 14:39:30
 */
@Getter
public class ByteArrayOutput implements Output {

    private int pos;
    private byte[] data;

    public ByteArrayOutput(byte[] data) {
        this.data = data;
    }

    @Override
    public void write(byte b) {
        if (remain() < 1) {
            throw new EOFException();
        }
        data[pos++] = b;
    }

    @Override
    public void write(byte[] bs, int off, int len) {
        if (remain() < len) {
            throw new EOFException();
        }
        System.arraycopy(bs, off, data, pos, len);
        pos += len;
    }

    private int remain() {
        return data.length - pos;
    }

}
