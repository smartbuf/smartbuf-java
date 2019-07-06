package com.github.sisyphsu.nakedata.convertor.codec.net;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * URL's codec
 *
 * @author sulin
 * @since 2019-05-13 18:55:13
 */
public class URLCodec extends Codec {

    /**
     * Convert String to URL
     *
     * @param s String
     * @return URL
     */
    public URL toURL(String s) {
        if (s == null)
            return null;
        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            throw new RuntimeException("Cant convert String to URL: " + s, e);
        }
    }

    /**
     * Convert URL to String
     *
     * @param url URL
     * @return String
     */
    public String toString(URL url) {
        if (url == null)
            return null;

        return url.toString();
    }

}
