package com.github.smartbuf.exception;

import java.io.IOException;

/**
 * InvalidVersionException indicates the specified data has an invalid version
 *
 * @author sulin
 * @since 2019-10-22 17:17:44
 */
public class InvalidVersionException extends IOException {

    public InvalidVersionException(int expectedVer, int ver) {
        super("Expect " + Integer.toHexString(expectedVer) + ", but received " + Integer.toHexString(ver));
    }

}
