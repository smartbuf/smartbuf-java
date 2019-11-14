package com.github.smartbuf.converter.codec;

import com.github.smartbuf.converter.Codec;
import com.github.smartbuf.converter.Converter;

import java.net.*;

/**
 * Codec for `java.net` package
 *
 * @author sulin
 * @since 2019-07-25 20:44:11
 */
public final class NetCodec extends Codec {

    @Converter
    public URI toURI(String s) {
        return URI.create(s);
    }

    @Converter
    public String toString(URI uri) {
        return uri.toString();
    }

    @Converter
    public URL toURL(String s) throws MalformedURLException {
        return new URL(s);
    }

    @Converter
    public String toString(URL url) {
        return url.toString();
    }

    /*
     * Convert String to InetAddress, support Inet4Address and Inet6Address
     */
    @Converter(extensible = true)
    public InetAddress toInetAddress(String s) throws UnknownHostException {
        int off = s.indexOf('/');
        if (off >= 0) {
            String hostname = s.substring(0, off);
            String address = s.substring(off + 1);
            s = hostname.length() == 0 ? address : hostname;
        }
        return InetAddress.getByName(s);
    }

    /*
     * Convert InetAddress to String, support Inet4Address and Inet6Address
     */
    @Converter
    public String toString(InetAddress addr) {
        return addr.getHostAddress();
    }

    @Converter
    public InetSocketAddress toInetSocketAddress(String s) {
        String host, port = null;
        int off = s.indexOf(':');
        if (off > 0) {
            host = s.substring(0, off);
            port = s.substring(off + 1);
        } else {
            host = s;
        }
        InetAddress addr = convert(host, InetAddress.class);
        if (port == null) {
            return new InetSocketAddress(addr, 0);
        }
        return new InetSocketAddress(addr, Integer.parseInt(port));
    }

    @Converter
    public String toString(InetSocketAddress addr) {
        return addr.getHostString() + ":" + addr.getPort();
    }

}
