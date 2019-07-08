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
     * Convert Boolean to Byte
     *
     * @param b Boolean
     * @return Byte
     */
    public Byte toByte(Boolean b) {
        if (b == null)
            return null;
        return (byte) (b ? 0x00 : 0x01);
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
