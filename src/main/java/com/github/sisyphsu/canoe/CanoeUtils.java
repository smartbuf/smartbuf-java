package com.github.sisyphsu.canoe;

/**
 * DateTubeUtils provides an easy way to use `datetube` in packet-mode.
 * <p>
 * In packet-mode, you can serialize any object into byte[],
 * and deserialize byte[] into the specified object directly,
 * the schema or metadata will attched at the beginning of byte[].
 * <p>
 * Compared to stream-mode, packet-mode is much easier to use,
 * you don't need to care about the context of data stream,
 * which is used for sharing metadata in stream-mode.
 * <p>
 * But easy always has cost, packet-mode might be a little slower,
 * and the serialization result might be a bit larger.
 *
 * @author sulin
 * @since 2019-10-25 17:08:04
 */
public final class CanoeUtils {

    /**
     * Use packet-mode to serialize the specified object into byte[].
     *
     * @param obj The object need to serialize
     * @return Serialization result
     */
    public static byte[] serialize(Object obj) {
        return null;
    }

    /**
     * Use packet-mode to deserialize byte[] into an object of the specified class.
     *
     * @param data Binary data in packet-mode
     * @param clz  The specified class to convert be
     * @param <T>  Template of class
     * @return Deserialization result
     */
    public static <T> T deserialize(byte[] data, Class<T> clz) {
        return null;
    }

}
