package com.github.sisyphsu.nakedata.convertor.codec.time.java8;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.time.ZoneId;

/**
 * ZoneId's codec
 *
 * @author sulin
 * @since 2019-05-10 10:46:11
 */
public class ZoneIdCodec extends Codec {

    /**
     * Convert String to ZoneId
     *
     * @param s String
     * @return ZoneId
     */
    public ZoneId toZoneId(String s) {
        return s == null ? null : ZoneId.of(s);
    }

    /**
     * Convert ZoneId to String
     *
     * @param zoneId ZoneId
     * @return String
     */
    public String toString(ZoneId zoneId) {
        return zoneId == null ? null : zoneId.getId();
    }

}
