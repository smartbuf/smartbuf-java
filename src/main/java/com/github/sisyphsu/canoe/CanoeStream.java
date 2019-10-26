package com.github.sisyphsu.canoe;

import com.github.sisyphsu.canoe.transport.Input;
import com.github.sisyphsu.canoe.transport.Output;

/**
 * StreamTube provides stream-mode implementation of "canoe" protocol.
 *
 * @author sulin
 * @since 2019-10-25 14:40:52
 */
public final class CanoeStream {

    private IOReader reader;
    private IOWriter writer;
    private Input    input;
    private Output   output;

    public <T> void write(T obj) {
    }

    public <T> T read(Class<T> clz) {
        return null;
    }

    public void close() {
    }

}
