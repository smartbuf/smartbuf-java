package com.github.sisyphsu.smartbuf.exception;

import java.io.IOException;

/**
 * CanoeClosedException represents the closed canoe instance are reused.
 *
 * @author sulin
 * @since 2019-10-28 17:22:11
 */
public class CanoeClosedException extends IOException {

    public CanoeClosedException(String message) {
        super(message);
    }

}
