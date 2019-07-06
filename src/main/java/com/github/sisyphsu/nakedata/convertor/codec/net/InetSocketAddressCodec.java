package com.github.sisyphsu.nakedata.convertor.codec.net;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InetSocketAddress's codec
 *
 * @author sulin
 * @since 2019-05-13 18:59:27
 */
public class InetSocketAddressCodec extends Codec {

    private static final Pattern PATTERN = Pattern.compile("(.*)[:(\\d+)]?");

    /**
     * Convert String to InetSocketAddress
     *
     * @param s String
     * @return InetSocketAddress
     */
    public InetSocketAddress toInetSocketAddress(String s) {
        if (s == null)
            return null;

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
     *
     * @param addr InetSocketAddress
     * @return String
     */
    public String toString(InetSocketAddress addr) {
        if (addr == null)
            return null;

        String addrStr = convert(addr.getAddress(), String.class);
        return addrStr + ":" + addr.getPort();
    }

}
