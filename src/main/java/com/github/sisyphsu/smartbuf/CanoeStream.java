package com.github.sisyphsu.smartbuf;

import com.github.sisyphsu.smartbuf.reflect.TypeRef;

import java.io.IOException;

/**
 * CanoeStream provides an easy way to use "canoe" protocol in stream-mode,
 * It's similar to {@link CanoePacket}, but support the context concept.
 * <p>
 * CanoeStream is not thread-safe, you have to use it in single thread and orderly.
 * Otherwise, it will go into error.
 *
 * @author sulin
 * @since 2019-10-25 14:40:52
 */
public final class CanoeStream {

    public Canoe canoe;

    /**
     * Initialize CanoeStream
     */
    public CanoeStream() {
        this.canoe = new Canoe(true);
    }

    /**
     * Use stream-mode to serialize the specified object into byte[].
     *
     * @param obj The object need to serialize
     * @return Serialization result
     * @throws IOException if an I/O error occurs.
     */
    public byte[] serialize(Object obj) throws IOException {
        return canoe.write(obj);
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
        return canoe.read(data, tClz);
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
        return canoe.read(data, tRef);
    }

    /**
     * Close this instance, would close the underlying canoe instance.
     */
    public void close() {
        canoe.close();
    }

}
