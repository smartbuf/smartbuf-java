package com.github.sisyphsu.canoe;

import java.io.IOException;
import java.io.InputStream;

/**
 * IOReader defines the specification of IO's read operation.
 * Caller should provider its implementation to adapt underlying transport layer.
 *
 * @author sulin
 * @since 2019-10-25 14:48:40
 */
@FunctionalInterface
public interface IOReader {

    /**
     * Read an new byte from underlying Reader, and it's similar to {@link InputStream#read()}
     *
     * @return the next byte of data, or <code>-1</code> if the end of the stream is reached.
     * @throws IOException if an I/O error occurs.
     */
    int read() throws IOException;

    /**
     * Reader could provide a optional closer.
     *
     * @throws IOException if an I/O error occurs.
     */
    default void close() throws IOException {
    }

}
