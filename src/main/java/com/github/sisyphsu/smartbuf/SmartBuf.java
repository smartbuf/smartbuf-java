package com.github.sisyphsu.smartbuf;

import com.github.sisyphsu.smartbuf.exception.SmartBufClosedException;
import com.github.sisyphsu.smartbuf.reflect.TypeRef;
import com.github.sisyphsu.smartbuf.transport.Input;
import com.github.sisyphsu.smartbuf.transport.Output;
import com.github.sisyphsu.smartbuf.utils.CodecUtils;

import java.io.IOException;

/**
 * SmartBuf wraps the "smartbuf" protocol, includes packet-mode and stream-mode.
 * <p>
 * In most cases, you can use {@link SmartPacket} and {@link SmartStream} directly.
 *
 * @author sulin
 * @since 2019-10-28 15:13:24
 */
public final class SmartBuf {

    public Input  input;
    public Output output;

    private volatile boolean closed;

    /**
     * Initialize SmartBuf instance, supports packet-mode and stream-mode
     *
     * @param enableStreamMode Enable stream-mode or not
     */
    public SmartBuf(boolean enableStreamMode) {
        this.input = new Input(enableStreamMode);
        this.output = new Output(enableStreamMode);
    }

    /**
     * Read the next object and convert it into the specified type.
     *
     * @param tRef The specified type, support generic type
     * @param <T>  Generic Type
     * @return Read result
     * @throws IOException if an I/O error occurs.
     */
    public <T> T read(byte[] data, TypeRef<T> tRef) throws IOException {
        if (closed) {
            throw new SmartBufClosedException("SmartBuf is closed");
        }
        Object obj = input.read(data);
        return CodecUtils.convert(obj, tRef);
    }

    /**
     * Read the next object and convert it into the specified class.
     *
     * @param tCls The specified class
     * @return Read result
     * @throws IOException if an I/O error occurs.
     */
    public <T> T read(byte[] data, Class<T> tCls) throws IOException {
        if (closed) {
            throw new SmartBufClosedException("SmartBuf is closed");
        }
        Object obj = input.read(data);
        return CodecUtils.convert(obj, tCls);
    }

    /**
     * Write the specified object into
     *
     * @param obj The specified object
     * @throws IOException if an I/O error occurs.
     */
    public byte[] write(Object obj) throws IOException {
        if (closed) {
            throw new SmartBufClosedException("SmartBuf is closed");
        }
        return output.write(obj);
    }

    /**
     * Close this instance, and release all resources
     */
    public void close() {
        if (this.closed) {
            return;
        }
        this.input = null;
        this.output = null;
        this.closed = true;
    }

}
