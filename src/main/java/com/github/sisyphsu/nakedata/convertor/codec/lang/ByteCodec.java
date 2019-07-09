package com.github.sisyphsu.nakedata.convertor.codec.lang;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

/**
 * Byte's codec
 *
 * @author sulin
 * @since 2019-05-13 18:12:20
 */
public class ByteCodec extends Codec {

    /**
     * Convert Long to Byte
     *
     * @param l Long
     * @return Byte
     */
    public Byte toByte(Long l) {
        return l == null ? null : l.byteValue();
    }

    /**
     * Convert Byte to Long
     *
     * @param b Byte
     * @return Long
     */
    public Long toLong(Byte b) {
        return b == null ? null : b.longValue();
    }

    /**
     * Convert byte[] to Byte[]
     *
     * @param arr byte[]
     * @return Byte[]
     */
    public Byte[] convert(byte[] arr) {
        if (arr == null) {
            return null;
        }
        Byte[] result = new Byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

    /**
     * Convert Byte[] to byte[]
     *
     * @param arr Byte[]
     * @return byte[]
     */
    public byte[] convert(Byte[] arr) {
        if (arr == null) {
            return null;
        }
        byte[] result = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = arr[i];
        }
        return result;
    }

}
