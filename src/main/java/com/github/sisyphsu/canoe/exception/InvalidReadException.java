package com.github.sisyphsu.canoe.exception;

import java.io.IOException;

/**
 * Exception that indicate Input run into some unexpected data.
 *
 * @author sulin
 * @since 2019-10-22 17:10:07
 */
public class InvalidReadException extends IOException {

    public InvalidReadException(String message) {
        super(message);
    }

}
