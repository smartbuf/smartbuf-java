package com.github.smartbuf;

import com.github.smartbuf.reflect.TypeRef;
import com.github.smartbuf.utils.CodecUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
     * User packet-mode to serialize the specified object and write the bytecodes into {@link OutputStream}
     *
     * @param obj          The object need to serialize
     * @param outputStream The output stream to write bytecodes
     * @throws IOException if an I/O error occurs.
     */
    public static void serialize(Object obj, OutputStream outputStream) throws IOException {
        SmartBuf buf = getLocalBuf();
        buf.write(obj, outputStream);
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

    /**
     * Use packet-mode to deserialize byte[] into an object of the specified type,
     *
     * @param data Binary data in packet-mode format
     * @param type The specified javaType to convert
     * @param <T>  Template of target type
     * @return Deserialization result
     * @throws IOException if an I/O error occurs.
     */
    public static <T> T deserialize(byte[] data, java.lang.reflect.Type type) throws IOException {
        SmartBuf buf = getLocalBuf();
        Object obj = buf.readObject(data);
        return CodecUtils.convert(obj, type);
    }

    /**
     * User packet-mode to deserialize InputStream into an object of the specified class
     *
     * @param inputStream The inputStream to read bytecodes
     * @param cls         The specified javaType to convert
     * @param <T>         Template of target type
     * @return Deserialization result
     * @throws IOException if an I/O error occurs.
     */
    public static <T> T deserialize(InputStream inputStream, Class<T> cls) throws IOException {
        SmartBuf buf = getLocalBuf();
        return buf.read(inputStream, cls);
    }

    /**
     * User packet-mode to deserialize InputStream into an object of the specified TypeRef type
     * could used for supporting generic type.
     *
     * @param inputStream The inputStream to read bytecodes
     * @param typeRef     The specified type to convert
     * @param <T>         Template of target type
     * @return Deserialization result
     * @throws IOException if an I/O error occurs.
     */
    public static <T> T deserialize(InputStream inputStream, TypeRef<T> typeRef) throws IOException {
        return deserialize(inputStream, typeRef.getType());
    }

    /**
     * User packet-mode to deserialize InputStream into an object of the specified javaType
     *
     * @param inputStream The inputStream to read bytecodes
     * @param type        The specified javaType to convert
     * @param <T>         Template of target type
     * @return Deserialization result
     * @throws IOException if an I/O error occurs.
     */
    public static <T> T deserialize(InputStream inputStream, java.lang.reflect.Type type) throws IOException {
        SmartBuf buf = getLocalBuf();
        Object obj = buf.readObject(inputStream);
        return CodecUtils.convert(obj, type);
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
