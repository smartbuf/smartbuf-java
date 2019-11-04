package com.github.sisyphsu.canoe.exception;

import java.io.IOException;

/**
 * Exception that indicate the sequence of input data is unexpected,
 * Maybe packet-lost occured, in this case, should reset all input&output context.
 *
 * @author sulin
 * @since 2019-10-22 17:00:39
 */
public class UnexpectedSequenceException extends IOException {

    public UnexpectedSequenceException(long local, long seq) {
        super("The sequence of schema-area is unexpected, expect " + local + ", but received " + seq);
    }

}
