package com.github.sisyphsu.nakedata.convertor.codec.net;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.net.Inet6Address;
import java.net.InetAddress;

/**
 * Inet6Address's codec
 *
 * @author sulin
 * @since 2019-05-13 19:00:30
 */
public class Inet6AddressCodec extends Codec {

    /**
     * Convert String to Inet6Address
     *
     * @param s String
     * @return Inet6Address
     */
    @Converter
    public Inet6Address toInet6Address(String s) {
        if (s == null)
            return null;

        try {
            return (Inet6Address) InetAddress.getByName(s);
        } catch (Exception e) {
            throw new RuntimeException("Cant convert String to Inet6Address: " + s, e);
        }
    }

    /**
     * Convert Inet6Address to String
     *
     * @param addr Inet6Address
     * @return String
     */
    @Converter
    public String toString(Inet6Address addr) {
        if (addr == null)
            return null;

        return addr.toString();
    }

}
