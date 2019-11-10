package com.github.sisyphsu.smartbuf.exception;

import java.io.IOException;

/**
 * MismatchModeException indicates the `mode` flag didn't match between input and output.
 *
 * @author sulin
 * @since 2019-10-22 16:49:56
 */
public class MismatchModeException extends IOException {

    public MismatchModeException(boolean localEnableStream) {
        super(localEnableStream ?
            "expect stream mode, but data has no-stream mode flag" :
            "expect no-stream mode, but data has stream mode flag");
    }

}
