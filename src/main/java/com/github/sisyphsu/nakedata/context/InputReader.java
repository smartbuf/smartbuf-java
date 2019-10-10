package com.github.sisyphsu.nakedata.context;

import java.io.InputStream;

/**
 * @author sulin
 * @since 2019-10-10 21:44:32
 */
public class InputReader {

    private final InputStream stream;

    public InputReader(InputStream stream) {
        this.stream = stream;
    }

}
