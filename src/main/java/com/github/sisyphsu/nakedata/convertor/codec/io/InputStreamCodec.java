package com.github.sisyphsu.nakedata.convertor.codec.io;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * InputStream's codec
 *
 * @author sulin
 * @since 2019-05-13 20:25:54
 */
public class InputStreamCodec extends Codec {

    /**
     * Convert byte[] to InputStream
     *
     * @param bytes byte[]
     * @return InputStream
     */
    @Converter
    public InputStream toInputStream(byte[] bytes) {
        return bytes == null ? null : new ByteArrayInputStream(bytes);
    }

    /**
     * Convert InputStream to byte[]
     *
     * @param is InputStream
     * @return byte[]
     */
    @Converter
    public byte[] toByteArray(InputStream is) {
        if (is == null) {
            return null;
        }
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[4096];
            for (int n; (n = is.read(buffer)) != 0; ) {
                output.write(buffer, 0, n);
            }
        } catch (IOException e) {
            throw new RuntimeException("Convert InputStream to byte[] failed", e);
        }
        return output.toByteArray();
    }

}
