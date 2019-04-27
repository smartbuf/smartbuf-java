package com.github.sisyphsu.nakedata.exception;

/**
 * @author sulin
 * @since 2019-04-27 14:52:33
 */
public class AssertException extends RuntimeException {
    public AssertException() {
    }

    public AssertException(String message) {
        super(message);
    }

    public AssertException(String message, Throwable cause) {
        super(message, cause);
    }

}
