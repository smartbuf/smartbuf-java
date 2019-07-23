package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.BitSet;

/**
 * BitSet's codec
 *
 * @author sulin
 * @since 2019-05-13 18:16:19
 */
public class BitSetCodec extends Codec {

    /**
     * Convert byte[] to BitSet
     *
     * @param bytes byte[]
     * @return BitSet
     */
    @Converter
    public BitSet toBitSet(byte[] bytes) {
        if (bytes == null)
            return null;
        return BitSet.valueOf(bytes);
    }

    /**
     * Convert BitSet to byte[]
     *
     * @param bitSet BitSet
     * @return byte[]
     */
    @Converter
    public byte[] toByteArray(BitSet bitSet) {
        if (bitSet == null)
            return null;
        return bitSet.toByteArray();
    }

}
