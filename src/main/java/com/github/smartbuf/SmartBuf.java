package com.github.smartbuf;

import com.github.smartbuf.exception.SmartBufClosedException;
import com.github.smartbuf.reflect.TypeRef;
import com.github.smartbuf.transport.Input;
import com.github.smartbuf.transport.Output;
import com.github.smartbuf.utils.CodecUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
     * Read the next object and convert it into the specified class.
     *
     * @param data The data to deserialize
     * @param tCls The specified class
     * @param <T>  Generic Type
     * @return Read result
     * @throws IOException if an I/O error occurs.
     */
    public <T> T read(byte[] data, Class<T> tCls) throws IOException {
        Object obj = readObject(data);
        return CodecUtils.convert(obj, tCls);
    }

    /**
     * Read the next object and convert it into the specified type.
     *
     * @param data The data to deserialize
     * @param tRef The specified type, support generic type
     * @param <T>  Generic Type
     * @return Read result
     * @throws IOException if an I/O error occurs.
     */
    public <T> T read(byte[] data, TypeRef<T> tRef) throws IOException {
        Object obj = readObject(data);
        return CodecUtils.convert(obj, tRef);
    }

    /**
     * Read a object from the specified data
     *
     * @param data The bytearray to read object
     * @return Read result
     * @throws IOException if an I/O error occurs.
     */
    public Object readObject(byte[] data) throws IOException {
        if (closed) {
            throw new SmartBufClosedException("SmartBuf is closed");
        }
        return input.read(data);
    }

    /**
     * Read an object from the specified {@link InputStream} and convert it into tCls's instance.
     *
     * @param inputStream The input stream to read bytecodes
     * @param tCls        The specified result type, support generic type
     * @param <T>         Result's generic type
     * @return The read T instance
     * @throws IOException if an I/O error occurs.
     */
    public <T> T read(InputStream inputStream, Class<T> tCls) throws IOException {
        Object obj = readObject(inputStream);
        return CodecUtils.convert(obj, tCls);
    }

    /**
     * Read an object from the specified {@link InputStream} and convert it into T instance.
     *
     * @param inputStream The input stream to read bytecodes
     * @param tRef        The specified result type, support generic type
     * @param <T>         Result's generic type
     * @return The read T instance
     * @throws IOException if an I/O error occurs.
     */
    public <T> T read(InputStream inputStream, TypeRef<T> tRef) throws IOException {
        Object obj = readObject(inputStream);
        return CodecUtils.convert(obj, tRef);
    }

    /**
     * Read an object from the specified {@link InputStream}
     *
     * @param inputStream The input stream to read bytecodes
     * @return The read object
     * @throws IOException if an I/O error occurs.
     */
    public Object readObject(InputStream inputStream) throws IOException {
        if (closed) {
            throw new SmartBufClosedException("SmartBuf is closed");
        }
        return input.read(inputStream);
    }

    /**
     * Encode/Serialize the specified object into byte array.
     *
     * @param obj The specified object
     * @return Serialization result
     * @throws IOException if an I/O error occurs.
     */
    public byte[] write(Object obj) throws IOException {
        if (closed) {
            throw new SmartBufClosedException("SmartBuf is closed");
        }
        return output.write(obj);
    }

    /**
     * Encode the specified object into byte array, and write it into the specified {@link OutputStream}
     *
     * @param obj The object to encode
     * @param os  The {@link OutputStream} to write bytecodes
     * @throws IOException if an I/O error occurs.
     */
    public void write(Object obj, OutputStream os) throws IOException {
        if (closed) {
            throw new SmartBufClosedException("SmartBuf is closed");
        }
        output.write(obj, os);
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
