package com.github.sisyphsu.smartbuf.exception;

import java.io.IOException;

/**
 * InvalidDataException indicates the specified data-id is invalid.
 *
 * @author sulin
 * @since 2019-11-08 11:34:08
 */
public class InvalidDataException extends IOException {

    public InvalidDataException(String message) {
        super(message);
    }

}
