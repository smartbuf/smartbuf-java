package com.github.smartbuf;

import com.github.smartbuf.reflect.TypeRef;

import java.io.IOException;

/**
 * SmartStream provides an easy way to use "smartbuf" protocol in stream-mode,
 * It's similar to {@link SmartPacket}, but support the context concept.
 * <p>
 * SmartStream is not thread-safe, you have to use it in single thread and orderly.
 * Otherwise, it will go into error.
 *
 * @author sulin
 * @since 2019-10-25 14:40:52
 */
public final class SmartStream {

    public final SmartBuf buf;

    /**
     * Initialize SmartStream
     */
    public SmartStream() {
        this.buf = new SmartBuf(true);
    }

    /**
     * Use stream-mode to serialize the specified object into byte[].
     *
     * @param obj The object need to serialize
     * @return Serialization result
     * @throws IOException if an I/O error occurs.
     */
    public byte[] serialize(Object obj) throws IOException {
        return buf.write(obj);
    }

    /**
     * Use stream-mode to deserialize byte[] into an object of the specified class.
     *
     * @param data Binary data in packet-mode format
     * @param tClz The specified class to convert
     * @param <T>  Template of target class
     * @return Deserialization result
     * @throws IOException if an I/O error occurs.
     */
    public <T> T deserialize(byte[] data, Class<T> tClz) throws IOException {
        return buf.read(data, tClz);
    }

    /**
     * Use stream-mode to deserialize byte[] into an object of the specified type,
     * could used for supporting generic type.
     *
     * @param data Binary data in packet-mode format
     * @param tRef The specified type to convert
     * @param <T>  Template of target type
     * @return Deserialization result
     * @throws IOException if an I/O error occurs.
     */
    public <T> T deserialize(byte[] data, TypeRef<T> tRef) throws IOException {
        return buf.read(data, tRef);
    }

    /**
     * Close this instance, would close the underlying SmartBuf instance.
     */
    public void close() {
        buf.close();
    }

}
