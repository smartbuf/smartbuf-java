package com.github.sisyphsu.nakedata.convertor.codec.io;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.io.File;

/**
 * File's codec
 *
 * @author sulin
 * @since 2019-05-13 20:25:16
 */
public class FileCodec extends Codec {

    /**
     * Convert File to String, toString directly
     */
    @Converter
    public String toString(File file) {
        return file == null ? null : file.toString();
    }

    /**
     * Convert String to File, should throw Exception
     */
    @Converter
    public File toFile(String s) {
        if (s == null) {
            return null;
        }
        throw new RuntimeException("can't convert String to File: " + s);
    }

}
