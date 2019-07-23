package com.github.sisyphsu.nakedata.convertor.codec.sql;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * Blob's codec
 *
 * @author sulin
 * @since 2019-05-13 18:10:14
 */
public class BlobCodec extends Codec {

    /**
     * Convert Blob to InputStream
     *
     * @param b Blob
     * @return InputStream
     */
    @Converter
    public InputStream toInputStream(Blob b) {
        if (b == null)
            return null;
        try {
            return b.getBinaryStream();
        } catch (SQLException e) {
            throw new RuntimeException("Convert Blob to InputStream failed.", e);
        }
    }

}
