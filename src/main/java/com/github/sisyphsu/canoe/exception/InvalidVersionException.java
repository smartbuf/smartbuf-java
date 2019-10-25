package com.github.sisyphsu.canoe.exception;

import java.io.IOException;

import static com.github.sisyphsu.canoe.transport.Const.VER;

/**
 * Exception that indicate the specified data has an invalid version
 *
 * @author sulin
 * @since 2019-10-22 17:17:44
 */
public class InvalidVersionException extends IOException {

    public InvalidVersionException(int ver) {
        super("Expect " + Integer.toHexString(VER) + ", but received " + Integer.toHexString(ver));
    }

}
