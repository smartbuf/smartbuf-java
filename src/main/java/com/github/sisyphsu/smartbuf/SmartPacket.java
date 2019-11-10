package com.github.sisyphsu.smartbuf;

import com.github.sisyphsu.smartbuf.reflect.TypeRef;

import java.io.IOException;

/**
 * SmartPacket provides an easy way to use `smartbuf` in packet-mode.
 * <p>
 * In packet-mode, you can serialize any object into byte[],
 * and deserialize byte[] into the specified object directly,
 * the schema or metadata will attched at the beginning of byte[].
 * <p>
 * Compared to stream-mode, packet-mode is much easier to use,
 * and you don't need to care about the context of data stream,
 * which is used for sharing metadata in stream-mode.
 * <p>
 * But easy always has cost, packet-mode might be a little slower,
 * and the serialization result might be a bit larger.
 *
 * @author sulin
 * @since 2019-10-25 17:08:04
 */
public final class SmartPacket {

    private static final ThreadLocal<SmartBuf> SMART_BUF_LOCAL = new ThreadLocal<>();

    private SmartPacket() {
    }

    /**
     * Use packet-mode to serialize the specified object into byte[].
     *
     * @param obj The object need to serialize
     * @return Serialization result
     * @throws IOException if an I/O error occurs.
     */
    public static byte[] serialize(Object obj) throws IOException {
        SmartBuf buf = getLocalBuf();
        return buf.write(obj);
    }

    /**
     * Use packet-mode to deserialize byte[] into an object of the specified class.
     *
     * @param data Binary data in packet-mode format
     * @param clz  The specified class to convert
     * @param <T>  Template of target class
     * @return Deserialization result
     * @throws IOException if an I/O error occurs.
     */
    public static <T> T deserialize(byte[] data, Class<T> clz) throws IOException {
        SmartBuf buf = getLocalBuf();
        return buf.read(data, clz);
    }

    /**
     * Use packet-mode to deserialize byte[] into an object of the specified type,
     * could used for supporting generic type.
     *
     * @param data Binary data in packet-mode format
     * @param ref  The specified type to convert
     * @param <T>  Template of target type
     * @return Deserialization result
     * @throws IOException if an I/O error occurs.
     */
    public static <T> T deserialize(byte[] data, TypeRef<T> ref) throws IOException {
        SmartBuf buf = getLocalBuf();
        return buf.read(data, ref);
    }

    // fetch the shared SmartBuf in the current thread
    private static SmartBuf getLocalBuf() {
        SmartBuf buf = SMART_BUF_LOCAL.get();
        if (buf == null) {
            buf = new SmartBuf(false);
            SMART_BUF_LOCAL.set(buf);
        }
        return buf;
    }

}
