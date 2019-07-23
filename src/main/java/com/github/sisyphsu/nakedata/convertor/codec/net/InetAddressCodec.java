package com.github.sisyphsu.nakedata.convertor.codec.net;

import com.github.sisyphsu.nakedata.convertor.Converter;
import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * InetAddress's codec
 *
 * @author sulin
 * @since 2019-05-13 19:00:08
 */
public class InetAddressCodec extends Codec {

    /**
     * Convert String to InetAddress
     *
     * @param s String
     * @return InetAddress
     */
    @Converter
    public InetAddress toInetAddress(String s) {
        if (s == null)
            return null;
        try {
            return InetAddress.getByName(s);
        } catch (Exception e) {
            throw new RuntimeException("Cant convert String to InetAddress: " + s, e);
        }
    }

    /**
     * Convert InetAddress to String
     *
     * @param addr InetAddress
     * @return String
     */
    @Converter
    public String toString(InetAddress addr) {
        if (addr == null)
            return null;
        return addr.toString();
    }

}
