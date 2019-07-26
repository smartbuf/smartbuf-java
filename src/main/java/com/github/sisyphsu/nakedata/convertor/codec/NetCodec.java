package com.github.sisyphsu.nakedata.convertor.codec;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.Codec;

import java.net.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Codec for `java.net` package
 *
 * @author sulin
 * @since 2019-07-25 20:44:11
 */
public class NetCodec extends Codec {

    private static final Pattern PATTERN = Pattern.compile("(.*)[:(\\d+)]?");

    /**
     * Convert String to URI
     */
    @Converter
    public URI toURI(String s) {
        return URI.create(s);
    }

    /**
     * Convert URI to String
     */
    @Converter
    public String toString(URI uri) {
        return uri.toString();
    }

    /**
     * Convert String to URL
     */
    @Converter
    public URL toURL(String s) throws MalformedURLException {
        return new URL(s);
    }

    /**
     * Convert URL to String
     */
    @Converter
    public String toString(URL url) {
        return url.toString();
    }

    /**
     * Convert String to Inet4Address
     */
    @Converter
    public Inet4Address toInet4Address(String s) throws UnknownHostException {
        return (Inet4Address) InetAddress.getByName(s);
    }

    /**
     * Convert Inet4Address to String
     */
    @Converter
    public String toString(Inet4Address addr) {
        return addr.getHostAddress();
    }

    /**
     * Convert String to Inet6Address
     */
    @Converter
    public Inet6Address toInet6Address(String s) throws UnknownHostException {
        return (Inet6Address) InetAddress.getByName(s);
    }

    /**
     * Convert Inet6Address to String
     */
    @Converter
    public String toString(Inet6Address addr) {
        return addr.toString();
    }


    /**
     * Convert String to InetAddress
     */
    @Converter
    public InetAddress toInetAddress(String s) throws UnknownHostException {
        return InetAddress.getByName(s);
    }

    /**
     * Convert InetAddress to String
     */
    @Converter
    public String toString(InetAddress addr) {
        return addr.toString();
    }

    /**
     * Convert String to InetSocketAddress
     */
    @Converter
    public InetSocketAddress toInetSocketAddress(String s) {
        Matcher matcher = PATTERN.matcher(s);
        if (!matcher.matches()) {
            throw new RuntimeException("Cant convert String to InetSocketAddress" + s);
        }
        InetAddress addr = convert(matcher.group(1), InetAddress.class);
        if (matcher.groupCount() < 2) {
            return new InetSocketAddress(addr, -1);
        }
        return new InetSocketAddress(addr, Integer.parseInt(matcher.group(2)));
    }

    /**
     * Convert InetSocketAddress to String
     */
    @Converter
    public String toString(InetSocketAddress addr) {
        String addrStr = convert(addr.getAddress(), String.class);
        return addrStr + ":" + addr.getPort();
    }

}
