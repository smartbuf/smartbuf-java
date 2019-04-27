package com.github.sisyphsu.nakedata.io;

/**
 * @author sulin
 * @since 2019-04-27 12:52:52
 */
public interface Input {

    byte read();

    int read(byte[] bs, int off, int len);

    default int read(byte[] bs) {
        return this.read(bs, 0, bs.length);
    }

}
