package com.github.sisyphsu.nakedata.convertor.codec.net;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.net.URI;

/**
 * URI's codec
 *
 * @author sulin
 * @since 2019-05-13 18:55:22
 */
public class URICodec extends Codec {

    /**
     * Convert String to URI
     *
     * @param s String
     * @return URI
     */
    @Converter
    public URI toURI(String s) {
        if (s == null)
            return null;

        return URI.create(s);
    }

    /**
     * Convert URI to String
     *
     * @param uri URI
     * @return String
     */
    @Converter
    public String toString(URI uri) {
        if (uri == null)
            return null;

        return uri.toString();
    }

}
