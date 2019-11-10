package com.github.sisyphsu.smartbuf.exception;

import java.io.IOException;

/**
 * SmartBufClosedException indicates the closed SmartBuf instance are reused.
 *
 * @author sulin
 * @since 2019-10-28 17:22:11
 */
public class SmartBufClosedException extends IOException {

    public SmartBufClosedException(String message) {
        super(message);
    }

}
