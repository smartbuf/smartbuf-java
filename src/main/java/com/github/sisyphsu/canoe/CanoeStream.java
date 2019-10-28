package com.github.sisyphsu.canoe;

import com.github.sisyphsu.canoe.reflect.TypeRef;

import java.io.IOException;

import static com.github.sisyphsu.canoe.Canoe.PACKET_LIMIT;

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

    private Canoe           canoe;
    private ByteArrayReader reader;
    private ByteArrayWriter writer;

    /**
     * Initialize CanoeStream
     */
    public CanoeStream() {
        this.reader = new ByteArrayReader();
        this.writer = new ByteArrayWriter(PACKET_LIMIT);
        this.canoe = new Canoe(true, reader, writer);
    }

    /**
     * Use stream-mode to serialize the specified object into byte[].
     *
     * @param obj The object need to serialize
     * @return Serialization result
     * @throws IOException if an I/O error occurs.
     */
    public byte[] serialize(Object obj) throws IOException {
        writer.reset();
        canoe.write(obj);
        return writer.toByteArray();
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
        reader.reset(data);
        return canoe.read(tClz);
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
        reader.reset(data);
        return canoe.read(tRef);
    }

    /**
     * Close this instance, would close the underlying canoe instance.
     *
     * @throws IOException if an I/O error occurs.
     */
    public void close() throws IOException {
        canoe.close();
    }

}
