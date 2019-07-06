package com.github.sisyphsu.nakedata.convertor.codec.net;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.net.Inet4Address;
import java.net.InetAddress;

/**
 * Inet4Address's codec
 *
 * @author sulin
 * @since 2019-05-13 19:00:21
 */
public class Inet4AddressCodec extends Codec {

    /**
     * Convert String to Inet4Address
     *
     * @param s String
     * @return Inet4Address
     */
    public Inet4Address toInet4Address(String s) {
        if (s == null)
            return null;
        try {
            return (Inet4Address) InetAddress.getByName(s);
        } catch (Exception e) {
            throw new RuntimeException("Can't convert String to Inet4Address: " + s, e);
        }
    }

    /**
     * Convert Inet4Address to String
     *
     * @param addr Inet4Address
     * @return String
     */
    public String toString(Inet4Address addr) {
        if (addr == null)
            return null;

        return addr.getHostAddress();
    }

}
