package com.github.sisyphsu.nakedata.convertor.codec.io;

import com.github.sisyphsu.nakedata.convertor.Converter;
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
     */
    @Converter
    public ByteBuffer toByteBuffer(byte[] bs) {
        return bs == null ? null : ByteBuffer.wrap(bs);
    }

    /**
     * Convert ByteBuffer to byte[]
     */
    @Converter
    public byte[] toByteArray(ByteBuffer buf) {
        return buf == null ? null : buf.array();
    }

}
