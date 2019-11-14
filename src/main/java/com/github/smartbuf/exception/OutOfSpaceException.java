package com.github.smartbuf.exception;

import java.io.IOException;

/**
 * OutOfSpaceException indicates that the data to write is too big to handle.
 *
 * @author sulin
 * @since 2019-10-27 15:55:25
 */
public class OutOfSpaceException extends IOException {

    public OutOfSpaceException(String message) {
        super(message);
    }

}
