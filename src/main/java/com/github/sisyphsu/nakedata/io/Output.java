package com.github.sisyphsu.nakedata.io;

/**
 * @author sulin
 * @since 2019-04-27 12:55:25
 */
public interface Output {

    void write(byte b);

    void write(byte[] bs, int off, int len);

    default void write(byte[] bs) {
        this.write(bs, 0, bs.length);
    }

}
