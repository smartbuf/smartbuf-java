package com.github.sisyphsu.nakedata.convertor.codec.util;

import com.github.sisyphsu.nakedata.convertor.codec.Codec;

import java.util.UUID;

/**
 * UUID's Codec
 *
 * @author sulin
 * @since 2019-05-13 18:17:39
 */
public class UUIDCodec extends Codec {

    /**
     * Convert String to UUID
     *
     * @param s String
     * @return UUID
     */
    public UUID toUUID(String s) {
        if (s == null)
            return null;

        return UUID.fromString(s);
    }

    /**
     * Convert UUID to String
     *
     * @param uuid UUID
     * @return String
     */
    public String toString(UUID uuid) {
        if (uuid == null)
            return null;
        return uuid.toString();
    }

}
