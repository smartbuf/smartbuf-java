package com.github.sisyphsu.canoe;

import com.github.sisyphsu.canoe.reflect.TypeRef;

import java.io.IOException;

/**
 * CanoePacket provides an easy way to use `canoe` in packet-mode.
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
public final class CanoePacket {

    static final ThreadLocal<Canoe> CANOE_LOCAL = new ThreadLocal<>();

    private CanoePacket() {
    }

    /**
     * Use packet-mode to serialize the specified object into byte[].
     *
     * @param obj The object need to serialize
     * @return Serialization result
     * @throws IOException if an I/O error occurs.
     */
    public static byte[] serialize(Object obj) throws IOException {
        Canoe canoe = getLocalCanoe();
        return canoe.write(obj);
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
        Canoe canoe = getLocalCanoe();
        return canoe.read(data, clz);
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
        Canoe canoe = getLocalCanoe();
        return canoe.read(data, ref);
    }

    // fetch the shared canoe in the current thread
    private static Canoe getLocalCanoe() {
        Canoe canoe = CANOE_LOCAL.get();
        if (canoe == null) {
            canoe = new Canoe(false);
            CANOE_LOCAL.set(canoe);
        }
        return canoe;
    }

}
