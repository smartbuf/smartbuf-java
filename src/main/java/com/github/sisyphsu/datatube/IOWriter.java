package com.github.sisyphsu.datatube;

import java.io.IOException;

/**
 * IOWriter defines the specification of IO's write operation.
 * Caller should provider its implementation to adapt underlying transport layer.
 *
 * @author sulin
 * @since 2019-10-25 14:48:59
 */
@FunctionalInterface
public interface IOWriter {

    /**
     * Read an new byte from underlying Reader, and it's similar to {@link java.io.OutputStream#write(int)}
     *
     * @param b the <code>byte</code>.
     * @throws IOException if an I/O error occurs.
     *                     In particular, an <code>IOException</code> may be thrown if the output stream has been closed.
     */
    void write(byte b) throws IOException;

}
