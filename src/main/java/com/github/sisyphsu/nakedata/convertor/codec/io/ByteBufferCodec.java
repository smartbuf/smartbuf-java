package com.github.sisyphsu.nakedata.convertor.codec.io;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.nio.ByteBuffer;

/**
 * ByteBuffers' codec
 *
 * @author sulin
 * @since 2019-05-13 18:24:00
 */
public class ByteBufferCodec extends Codec {

    /**
     * Convert byte[] to ByteBuffer
     *
     * @param bs byte[]
     * @return ByteBuffer
     */
    public ByteBuffer toByteBuffer(byte[] bs) {
        if (bs == null)
            return null;
        return ByteBuffer.wrap(bs);
    }

    /**
     * Convert ByteBuffer to byte[]
     *
     * @param buf ByteBuffer
     * @return byte[]
     */
    public byte[] toByteArray(ByteBuffer buf) {
        if (buf == null)
            return null;
        return buf.array();
    }

}
