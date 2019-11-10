package com.github.sisyphsu.smartbuf.exception;

import java.io.IOException;

/**
 * @author sulin
 * @since 2019-11-08 14:18:56
 */
public class InvalidStructException extends IOException {

    public InvalidStructException(String message) {
        super(message);
    }
}
